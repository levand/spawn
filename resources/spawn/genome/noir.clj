(ns spawn.genome.noir
  (:require [spawn.genome :as gene]))

(gene/defgenome noir
  "A genome for creating Noir web applications."
  [name]
  (let [dir (gene/underscore name)
        data {:name name}]
    {name {"project.clj" (gene/render "project.clj" data)
           ".gitignore"  (gene/render "gitignore" data)
           "README.md"   (gene/render "README.md" data)
           "src"         {dir {"server.clj" (gene/render "server.clj" data)
                               "views" {"welcome.clj" (gene/render "welcome.clj" data)
                                        "common.clj" (gene/render "common.clj" data)}
                               "models" {}}}
           "test"        {dir {"test" {}}}
           "resources"   {"public"  {"js" {}
                                     "css" {"reset.css" (gene/render "reset.css" data)}
                                     "img" {}}}}}))

