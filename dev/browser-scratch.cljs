(ns browser.scratch)


(def mike (.init (new ToggleButton
               app.core.antennaButton
               app.core.antennaBackground
               (fn [] (set! app.scenes.antenna.visible false))
               (fn [] (set! app.scenes.antenna.visible true)))))
               

(deftype ToggleButton [ button backing onActive onDeactive ] Object
    (init [ this  ]
      (set! this.button.onclick
            (fn [evt] (.onclick this evt)))
      (set! this.clicked false)
      this)

    (onclick [this evt ]
      (.preventDefault evt)
      (if (= this.clicked false)
        (.activate this)
        (.deactivate this)))

    (activate [ this ]
      (set! this.clicked true)
      (set! this.backing.style.opacity "0")
      (.onActive this))

    (deactivate [ this ]
      (set! this.clicked false)
      (set! this.backing.style.opacity "1")
      (.onDeactive this)))
    
