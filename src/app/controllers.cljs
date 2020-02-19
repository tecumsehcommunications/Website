(ns app.controllers)

(deftype Spinner [ container childArr ] Object
    (init [this]
       (set! this.active false)       
    this)

  (runit [ this el idx arr ]
    (set! el.style.animationPlayState "running"))

  (stopit [ this el idx arr ]
    (set! el.style.animationPlayState "paused"))

  (run [ this ]
    (set! this.active true)
    (set! this.container.style.display "block")
    (.forEach this.childArr this.runit))

  (stop [ this ]
    (set! this.container.style.display "none")
    (.forEach this.childArr this.stopit)
    (set! this.active false)))


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
    

(deftype ControlButton [ button target onActivate onDeactivate onAfterUp onAfterDown] Object
    (init [ this backing controlsList step ]
      (set! this.button.onclick (fn [evt] (.onclick this evt)))
      (set! this.clicked false)
      (set! this.controlsList controlsList)
      (set! this.backing backing)
      (set! this.active false)
      (set! this.blocking false)
      (set! this.blocked false)
      (set! this.step step)
      (set! this.max 1)
      this)

    (onclick [this evt ]
      (if evt (.preventDefault evt))
      (set! this.blocked false) 
      (if (. this -clicked)
       nil  ; if already clicked on, don't do anything
       (do
         (doseq [b this.controlsList]
           (if (aget b "controller" "clicked")
             (if (.-blocking b)
               (set! this.blocked true)
               (.deactivate (aget b "controller")))))
         (if-not this.blocked
           (.activate this)))))
    
   (onpulse [this]
     (if (. this -active)
       (if (. this -clicked)
         (let [ opc (js.parseFloat this.target.style.opacity)]
           (if (>= opc this.max)
             (do
               (set! this.target.style.opacity 1)
               (set! this.active false)
               (if this.onAfterUp (.onAfterUp this))) 
             
             (do
               (set! this.target.style.opacity
                     (+ (* 2 this.step) opc)))))

         (let [ opc (js.parseFloat this.target.style.opacity)]
           (if (<= opc 0)
             (do
               (set! this.target.style.opacity 0)
               (set! this.target.style.display "none")
               (set! this.active false)
               (if this.onAfterDown (.onAfterDown this)))
             
             (do
               (set! this.target.style.opacity
                     (- opc (* 2 this.step)))))))))

   (activate [ this ]
       (set! this.clicked true)
       (set! this.backing.style.opacity 1)
       (if this.target
         (do
           (set! this.target.style.display nil) ; default display
           (set! this.active true))); if there is a target for onpulse, activate onpulse
       (if this.onActivate (.onActivate this)))
               
   (deactivate [ this ]
       (set! this.clicked false)
       (if this.target (set! this.active true))
       (set! this.backing.style.opacity 0)
       (if this.onDeactivate (.onDeactivate this))))


