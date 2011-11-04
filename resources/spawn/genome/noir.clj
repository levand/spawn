(ns spawn.genome.noir
  (:require [spawn.genome :as gene]))

(defn- t
  "Utility function for building a local template path"
  [name]
  (str "spawn/genome/noir/" name))

(def noir-files ["project.clj" "README.md" "gitignore" "reset.css" "server.clj"
                 "welcome.clj" "common.clj"])

(defn file-map [fs data]
  (into {} (map (juxt keyword #(gene/apply-template (t %) data)) fs)))

(defn noir
  "A genome for creating webapps"
  [name]
  (let [data {:name name}
        dirname (gene/underscore name)
        files (file-map noir-files data)]
    (gene/build-filesystem
      {name {"project.clj" (:project.clj files)
             ".gitignore"  (:gitignore files)
             "README.md"      (:README.md files)
             "src"         {dirname {"server.clj" (:server.clj files)
                                     "views" {"welcome.clj" (:welcome.clj files)
                                              "common.clj" (:common.clj files)}
                                     "models" {}}}
             "test"        {dirname {"test" {}}}
             "resources"   {"public"  {"js" {}
                                       "css" {"reset.css" (:reset.css files)}
                                       "img" {}}}}})))

