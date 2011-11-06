(ns spawn.genome.cake-plugin
  (:require [spawn.genome :as gene]))

(gene/defgenome cake-plugin
  "A genome for creating cake plugins."
  [name]
  (let [name (if (.startsWith name "cake-") name (str "cake-" name))
        unprefixed-name (subs name 5)
        data {:name name
              :unprefixed-name unprefixed-name}]
    {name {"project.clj" (gene/render "project.clj" data)
           "README.md" (gene/render "README.md" data)
           ".gitignore" (gene/render "gitignore" data)
           "src" {"cake" {"tasks" {(str unprefixed-name ".clj") (gene/render "name.clj" data)}}}}}))