# Spawn

tl;dr: it can generate projects for you. Like 'cake new' or 'lein new' on steroids.

Spawn is a tool for generating projects out of simple templates. It supports automated project and code generation, similar to the scaffolding and project generation features found in Rails.

It has two primary goals:

1. To provide templated projects as a learning tool for Clojure newbies, allowing them to start building applications (such as web apps) right away. Figuring out how to assemble the relevant components can be a daunting task for someone lacking prior Clojure experience. 
2. To save time for experienced Clojure users, allowing them to stub out common application types in seconds instead of having to tediously build out the same infrastructure every time.

## Extensible

Spawn is very extensible. You can create your own templates (called genomes) and use them wherever you want. To do that, create a file on the classpath using a directory structure like this: `spawn/genome/mygenome.clj`. This file must contain a function that is the same name as the last segment of its namespace -- in this case, `mygenome`. If the first argument to this function is called 'project', then `spawn.core/spawn` will try to pass a project in, usually provided by either the cake or leiningen plugins.
