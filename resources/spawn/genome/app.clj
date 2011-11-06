(ns spawn.genome.app
  (:require [spawn.genome :as gene]))

(gene/defgenome app
  "A genome for creating average projects, like for a desktop application."
  [name]
  (let [data {:name name}]
    {name {"project.clj" (gene/render "project.clj" data)
           "README.md" (gene/render "README.md" data)
           ".gitignore" (gene/render "gitignore" data)
           "src" {(gene/underscore name) {"core.clj" (gene/render "core.clj" data)}}}}))