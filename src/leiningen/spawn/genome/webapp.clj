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
            "src"         {name {"server.clj" (gene/apply-template (t "server_clj") data)
                                 "controllers" {}
                                 "views" {}}}
            "test"        {name {"test" {"server.clj" (gene/apply-template (t "server_clj_test") data)}
                                 "controllers" {}
                                 "views" {}}}
            "public"      {"robots.txt" (gene/apply-template (t "robots_txt") data)
                           "js" {}
                           "css" {}
                           "img" {}}
            }})))