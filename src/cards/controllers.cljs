(ns cards.controllers)


  (deftype ScrollBar [ panel target ] Object           
         (init [ this ]   
           (set! this.touched false)
           (set! this.lastY 0)
           (set! this.target.style.top "0px")
           (set! this.panel.onpointerdown
                 (fn [evt] (.onpointerdown this evt)))
           (set! this.panel.onpointerup
                 (fn [evt] (.onpointerup this evt)))
           (set! this.panel.onpointermove
                 (fn [evt] (.onpointermove this evt)))
           (set! this.panel.onpointerleave
                 (fn [evt] (.onpointerleave this evt)))
           this)
         
         (onpointerup [ this evt ]
           (.preventDefault evt)
           (set! this.touched false))
             
         (onpointerleave [ this evt ]
           (.preventDefault evt)
           (set! this.touched false))

         (onpointerdown [ this evt ]
           (set! this.lastY evt.clientY)
           (set! this.touched true))  

         (onpointermove [ this evt ] ; reminder: Y increases positively from zero from top down
           (.preventDefault evt)
           (if this.touched
             (let [ tHeight this.target.offsetHeight
                    wHeight js/window.innerHeight
                    top (js/parseInt this.target.style.top) 
                    delta (- this.lastY evt.clientY) ]
               (set! this.lastY evt.clientY)
               (if (<= delta 0) ; delta negative, moving down. 
                 (let [ left2go (- tHeight wHeight (- top)) ]     
 ;                  (.log js/console tHeight " " wHeight " " top " " left2go " " delta )
                   (if (>= left2go 0) ;if moving down and still room to go
                     (if (<= (- delta) left2go)
                       (set! this.target.style.top (str (+ delta top) "px"))
                       (set! this.target.style.top (str (- top left2go) "px")))))
                 (if (< (+ top delta) 0); delta assumed positive - moving up, check if near top
                   (set! this.target.style.top (str (+ top delta) "px"))
                   (set! this.target.style.top (str 0 "px"))))))))                 
  
(deftype ToggleButton [ button active?] Object
   (init [ this  ]
     (if-not this.active?
       (do
         (set! this.button.style.opacity "0")
         (set! this.clicked false))
       (do
         (set! this.clicked true)))
      (set! this.button.onclick
            (fn [evt] (.onclick this evt)))
      this)

    (onclick [this evt ]
      (.preventDefault evt)
      (if (= this.clicked false)
        (.activate this)
        (.deactivate this)))

    (activate [ this ]
      (set! this.clicked true)
      (set! this.button.style.opacity "1"))

    (deactivate [ this ]
      (set! this.clicked false)
      (set! this.button.style.opacity "0")))


  (deftype ToggleRow [ button columns] Object
    (init [ this  ]
      (if (.includes this.button.parentElement.className "chinese")
        (set! this.clicked true)
        (set! this.clicked false))
      (set! this.button.onclick
            (fn [evt] (.onclick this evt)))
      this)

    (onclick [this evt ]
      (.preventDefault evt)
      (if (= this.clicked false)
        (.activate this)
        (.deactivate this)))

    (activate [ this ]
      (set! this.clicked true)
      (.forEach this.columns (fn [ el idx arr ]
                               (.activate (aget el "children" 0 "controller")))))
    (deactivate [ this ]
      (set! this.clicked false)
      (.forEach this.columns (fn [ el idx arr ]
                               (.deactivate (aget el "children" 0 "controller"))))))
