(ns leiningen.spawn.genome.webapp
  (:require [leiningen.spawn.genome-util :as gene]))

(defn- t
  "Utility function for building a local template path"
  [name]
  (str "leiningen/spawn/genome/webapp/" name))

(defn webapp
  "A genome for creating webapps"
  [name]
  (let [data {"name" name}]
    (gene/build-filesystem
     {name {"project.clj" (gene/apply-template (t "project_clj") data)
            "README"      (gene/apply-template (t "README") data)
            "src"         {name {"server.clj" (gene/apply-template (t "src/server_clj") data)
                                 "controllers" {"index.clj" (gene/apply-template (t "src/controllers/index_clj") data)}
                                 "views" {}}}
            "test"        {name {"test" {"server.clj" (gene/apply-template (t "test/server_clj_test") data)}}}
            "public"      {"robots.txt" (gene/apply-template (t "public/robots_txt") data)
                           "js" {}
                           "css" {}
                           "img" {}}
            }})))