(ns cards.core
  (:require  
           ;  [cards.repl :as repl]
             [cards.controllers :as controllers]))

(defn ^:export main [ ]
  
  (def flashCards (.getElementById js/document "flashCards"))
  (def scrollPanel (.getElementById js/document "scrollPanel"))
  (def scrollController (.init (new controllers/ScrollBar scrollPanel flashCards)))
  (def http (new js/XMLHttpRequest))
  (.open http "GET" "http://tecumsehcommunications.com:5984/cards/_design/dDoc/_view/v1")
  (def charData (js-obj))

  
  (defn putPage [ rowData ]
    (set! flashCards.innerHTML "")
    (let [ rows (.slice rowData)
           chunkSize 7
           splices (.ceil js/Math (/ rows.length chunkSize))
           chunks (array) ]
      (doseq [ x (range splices) ] (aset chunks x (.splice rows 0 chunkSize)))
      (doseq [ x chunks ]
        (let [ node  (.createElement js/document "DIV") ]
         (set! node.innerHTML (str                                               
           "<div class=\"row english\">
             <div class=\"switch\"></div> 
             <div class=\"column light\" ><p>"(aget x 0 "value" "english")"</p></div>
             <div class=\"column medium\" ><p>"(aget x 1 "value" "english")"</p></div>
             <div class=\"column regular\" ><p>"(aget x 2 "value" "english")"</p></div>
             <div class=\"column dark\" ><p>"(aget x 3 "value" "english")"</p></div>
             <div class=\"column light\" ><p>"(aget x 4 "value" "english")"</p></div>
             <div class=\"column medium\" ><p>"(aget x 5 "value" "english")"</p></div>
             <div class=\"column regular\" ><p>"(aget x 6 "value" "english")"</p></div>         
           </div>
           <div class=\"row chinese\">
             <div class=\"switch\"></div>
             <div class=\"column\" ><p>"(aget x 0 "value" "chinese")"</p></div>
             <div class=\"column\" ><p>"(aget x 1 "value" "chinese")"</p></div>
             <div class=\"column\" ><p>"(aget x 2 "value" "chinese")"</p></div>
             <div class=\"column\" ><p>"(aget x 3 "value" "chinese")"</p></div>
             <div class=\"column\" ><p>"(aget x 4 "value" "chinese")"</p></div>
             <div class=\"column\" ><p>"(aget x 5 "value" "chinese")"</p></div>
             <div class=\"column\" ><p>"(aget x 6 "value" "chinese")"</p></div>
           </div>
           <div class=\"row pinyin\">
             <div class=\"switch\"></div>
             <div class=\"column dark\" ><p>"(aget x 0 "value" "pinyin")"</p></div>
             <div class=\"column regular\" ><p>"(aget x 1 "value" "pinyin")"</p></div>  
             <div class=\"column medium\" ><p>"(aget x 2 "value" "pinyin")"</p></div>
             <div class=\"column light\" ><p>"(aget x 3 "value" "pinyin")"</p></div>
             <div class=\"column dark\" ><p>"(aget x 4 "value" "pinyin")"</p></div>
             <div class=\"column regular\" ><p>"(aget x 5 "value" "pinyin")"</p></div>
             <div class=\"column medium\" ><p>"(aget x 6 "value" "pinyin")"</p></div>
           </div>"))
         (.appendChild flashCards node))))
    
    (let [ characters (.querySelectorAll js/document ".chinese P")
           definitions (.querySelectorAll js/document ".english P")
           pinyin (.querySelectorAll js/document ".pinyin P")
           rows (.querySelectorAll js/document ".row") ]
      
      (.forEach characters (fn [ el idx arr ] (set! el.controller
                                                    (.init (new controllers/ToggleButton el true)))))
      (.forEach definitions (fn [ el idx arr ] (set! el.controller
                                                     (.init (new controllers/ToggleButton el false)))))
      (.forEach pinyin (fn [ el idx arr ] (set! el.controller
                                            (.init (new controllers/ToggleButton el false)))))
      (.forEach rows (fn [ el0 idx0 arr0 ]
                        (let [ columnArray (array)
                               button (atom nil)
                               childArray (.from js/Array (.-children el0)) ]
                          (.forEach childArray (fn [ el1 idx1 arr1 ]
                                                (if (= el1.className "switch")
                                                  (reset! button el1)
                                                  (.push columnArray el1))))
                          (aset @button  "controller"
                                (.init (new controllers/ToggleRow @button columnArray))))))))
  
    
  (set! http.onreadystatechange
        (fn [evt]
          (if (= http.readyState 4)
            (do
              (aset charData "rows" (.-rows (.parse js/JSON http.responseText)))
              (putPage charData.rows)))))

  (set! scrollPanel.ondblclick (fn [evt] (putPage (clj->js (clojure.core/shuffle charData.rows)))))

  
  (.send http)

  
  
)

