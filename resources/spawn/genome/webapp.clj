(ns spawn.genome.webapp
  (:require [spawn.genome :as gene]))

(defn- t
  "Utility function for building a local template path"
  [name]
  (str "spawn/genome/webapp/" name))

(defn webapp
  "A genome for creating webapps"
  [name]
  (let [data {:name name}
        dirname (gene/underscore name)]
    (gene/build-filesystem
     {name {"project.clj" (gene/apply-template (t "project.clj") data)
            "README"      (gene/apply-template (t "README.markdown") data)
            "src"         {dirname {"server.clj" (gene/apply-template (t "src/server.clj") data)
                                    "controllers" {"index.clj" (gene/apply-template (t "src/controllers/index.clj") data)}
                                     "views" {}}}
            "test"        {dirname {"test" {"server.clj" (gene/apply-template (t "test/server-test.clj") data)}}}
            "public"      {"robots.txt" (gene/apply-template (t "public/robots.txt") data)
                           "js" {}
                           "css" {}
                           "img" {}}}})))
