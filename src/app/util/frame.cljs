(ns app.util.frame )

(deftype FrameLoop [ clock ] Object
        (init [ this ]
          (set! this.reqId nil)
          (set! this.xArray (array))
          (set! this.run (.bind this.run this))
        this)

        (run [ this ]
          (set! this.reqId (js.requestAnimationFrame this.run))
          (.forEach this.xArray (fn [ el idx arr]
                                  (if el.active (.run el))))
          (.update this.clock (js/performance.now)))

        (stop [this]
              (js/cancelAnimationFrame this.reqId)))

