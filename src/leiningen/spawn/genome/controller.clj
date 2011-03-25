(ns leiningen.spawn.genome.controller
  (:require [leiningen.spawn.genome-util :as gene]
            [clojure.contrib.str-utils2 :as str]))

(defn- t
  "Utility function for building a local template path"
  [name]
  (str "leiningen/spawn/genome/controller/" name))

(defn insert-after [base pattern s]
  "Inserts s immediately after the first occurence of pattern in base"
  (let [re (str "\\Q" pattern "\\E")
        pat (re-pattern re)
        splitted (str/split base pat)]
    (reduce str [(first splitted) pattern s (second splitted)])))

(defn add-controller-ns [file projectname name]
  (let [pattern "[compojure.handler :as handler]"
        insert (str "\n            [" projectname ".controllers." name " :as " name "]")]
    (insert-after file pattern insert)))

(defn add-controller-routes [file name]
  (let [pattern "(GET \"/\" [] index/index-routes)"
        insert (str "\n  (GET \"/" name "/*\" [] " name "/" name "-routes)")]
    (insert-after file pattern insert)))

(defn update-server-file [projectname name]
  (let [filename (str "./src/" projectname "/server.clj")
        serverfile (slurp filename)]
    (if (or (not serverfile) (empty? serverfile))
      (throw (Exception. "Could not find server.clj. Did you generate this project with 'spawn webapp'?"))
      (spit filename (add-controller-routes (add-controller-ns serverfile projectname name) name)))))

(defn controller
  "Creates a controller in a webapp"
  [project name]
  (let [projectname (:name project)
        data {"name" name
              "projectname" projectname}]
    (update-server-file projectname name)
    (gene/build-filesystem
     {"src" {projectname {"controllers" {(str name ".clj") (gene/apply-template (t "template_clj") data)}}}})))
