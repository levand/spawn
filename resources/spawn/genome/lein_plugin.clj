(ns spawn.genome.lein-plugin
  (:require [spawn.genome :as gene]))

(gene/defgenome lein-plugin
  "A genome for creating Leiningen plugins."
  [name]
  (let [unprefixed-name (if (.startsWith name "lein-")
                          (subs name 5)
                          name)
        data {:name name
              :unprefixed-name unprefixed-name}]
    {name {"project.clj" (gene/render "project.clj" data)
           "README.md" (gene/render "README.md" data)
           ".gitignore" (gene/render "gitignore" data)
           "src" {"leiningen" {(str unprefixed-name ".clj") (gene/render "name.clj" data)}}}}))