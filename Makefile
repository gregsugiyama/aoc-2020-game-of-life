repl:
	clj -A:nREPL -M --main nrepl.cmdline --interactive --middleware [cider.nrepl/cider-middleware,cider.piggieback/wrap-cljs-repl]

clj-test:
	bin/kaocha clj/unit --watch

