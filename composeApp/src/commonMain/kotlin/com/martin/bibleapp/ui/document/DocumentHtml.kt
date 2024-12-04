package com.martin.bibleapp.ui.document

object DocumentHtml {
    private const val TEXT_COLOUR_PLACEHOLDER = "TEXT_COLOUR"

    fun formatHtmlPage(addJs: Boolean, html: String): String {
        val js = if (addJs) JAVA_SCRIPT else ""
        return "<html><head>$STYLE</head><body>$html $js</body></html>"
    }

    private val STYLE = """
        <style>
            body { font-size: 16pt; line-height: 1.8; margin: 0px 20px 0px 20px; color: $TEXT_COLOUR_PLACEHOLDER }
            p { padding:0px; margin:0px; text-indent: 5% }
            span.verse-no { vertical-align: super; font-size: 0.6em; font-weight: 300 }
        </style>
        """.trimIndent()

    private val JAVA_SCRIPT = """
        <div id="bottomOfBibleText" style="height: 100px"/>
        
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
        const options = {
          root: null,
          rootMargin: "100px",
          threshold: 0,
        };
        
        const observer = new IntersectionObserver(entries => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    window.kmpJsBridge.callNative(
                        "${InfiniteScrollJsMessageHandler.INFINITE_SCROLL_BRIDGE_METHOD}",
                        "bottom",
                        function (data) {
                            entry.target.insertAdjacentHTML('beforebegin', data);
                            verseOffsets = null; 
                        }
                    );
                }
            })
        })

        observer.observe(document.getElementById('bottomOfBibleText'))

        </script>          
""".trimIndent()

    fun updateTextColour(html: String, colour: String): String = html.replace(TEXT_COLOUR_PLACEHOLDER, colour)
}