package com.martin.bibleapp.ui.document

object DocumentHtml {
    private const val TEXT_COLOUR_PLACEHOLDER = "TEXT_COLOUR"

    fun formatHtmlPage(addJs: Boolean, html: String): String {
        val js = if (addJs) JAVA_SCRIPT else ""
        return "<html><head>$STYLE</head><body>$BEFORE_TEXT $html $AFTER_TEXT $js</body></html>"
    }

    private val STYLE = """
        <style>
            body { font-size: 16pt; line-height: 1.8; margin: 0px 20px 0px 20px; color: $TEXT_COLOUR_PLACEHOLDER }
            p { padding:0px; margin:0px; text-indent: 5% }
            span.verse-no { vertical-align: super; font-size: 0.6em; font-weight: 300 }
        </style>
        """.trimIndent()

    private val BEFORE_TEXT = """
        <div id="topOfBibleText" style="height: 100px"></div>
        <div id="container">
        """

    private val AFTER_TEXT = """
        <div id="bottomOfBibleText" style="height: 100px"></div>
        </div>
        """

    private val JAVA_SCRIPT = """
        <script language="javascript">
        window.onscroll = function() { onScrollHandler() };
        
        function onScrollHandler() {
            let currentVerse = getCurrentVerse(Math.floor( document.body.scrollTop ));
            
            window.kmpJsBridge.callNative(
                "${CurrentVerseJsMessageHandler.CURRENT_VERSE_BRIDGE_METHOD}",
                currentVerse,
                ""
            );
        }

        function getCurrentVerse(offset) {
            return getVerseOffsets().find(function (el) {
                return el.offset >= offset;
            })?.osisId;           
        }
        
        var verseOffsets = null;
        function getVerseOffsets() {
            if (verseOffsets) {
                return verseOffsets;
            }
            
            console.log("Calculate verse offsets");

            var verseNodeList = document.querySelectorAll('span.verse-no');
            let tempOffsets = [];
            for (let i=0; i < verseNodeList.length; i++) {
              let node = verseNodeList[i];
              const record = {
                 osisId: node.id,
                 offset: Math.floor(node.getBoundingClientRect().top + window.scrollY)
              }
              tempOffsets.push(record);
            }
            verseOffsets = tempOffsets;
            return verseOffsets
        }
        
        // Infinite scrolling
        //

        // Delaying topload during drag is required for iOS to maintain existing position
        var topLoadPending = false;
        var touchOccurring = false
        window.addEventListener('touchstart', function(e) {
           touchOccurring = true;
        });
        
        window.addEventListener('touchend', function(e) {
           if (topLoadPending) {
               topLoadPending = false;
               loadMoreAtTop();
           }
           touchOccurring = false;
        }); 
       
        const observer = new IntersectionObserver(entries => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    if (entry.target.id === 'topOfBibleText') {
                        console.log("Loading at top");
                        if (touchOccurring) {
                            topLoadPending = true;
                            return;
                        }
                        loadMoreAtTop() 
                    } else if (entry.target.id === 'bottomOfBibleText') {
                        window.kmpJsBridge.callNative(
                            "${InfiniteScrollJsMessageHandler.INFINITE_SCROLL_BRIDGE_METHOD}",
                            "${InfiniteScrollJsMessageHandler.NEXT_PAGE}",
                            function (data) {
                                entry.target.insertAdjacentHTML('beforebegin', data);
                                verseOffsets = null; 
                                registerLoadMoreObservers();
                            }
                        );
                    }
                }
            })
        })
        
        function loadMoreAtTop() {
            window.kmpJsBridge.callNative(
                "${InfiniteScrollJsMessageHandler.INFINITE_SCROLL_BRIDGE_METHOD}",
                "${InfiniteScrollJsMessageHandler.PREVIOUS_PAGE}",
                function (data) {
                    var priorHeight = document.body.scrollHeight;
                    var currentPos = window.pageYOffset;

                    var container = document.getElementById('container');
                    container.innerHTML = data + container.innerHTML;
                     
                    var changeInHeight = document.body.scrollHeight - priorHeight;
                    var adjustedCurrentPos = currentPos + changeInHeight;
                    window.scrollTo(0, adjustedCurrentPos);
                    verseOffsets = null;
                    registerLoadMoreObservers();
                }
            );
        }

        const INTERSECTION_OBSERVER_OPTIONS = { root: null, rootMargin: "1px", threshold: 0 };
        function registerLoadMoreObservers() {
            console.log("Registering load more observers");
            observer.disconnect()
            observer.observe(document.getElementById('topOfBibleText'), INTERSECTION_OBSERVER_OPTIONS)
            observer.observe(document.getElementById('bottomOfBibleText'), INTERSECTION_OBSERVER_OPTIONS)
        }
        
        registerLoadMoreObservers();
        </script>          
""".trimIndent()

    fun updateTextColour(html: String, colour: String): String = html.replace(TEXT_COLOUR_PLACEHOLDER, colour)
}