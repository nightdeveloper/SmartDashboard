DiscoveryTab = function () {

    this.init = function () {

        var button = $("#content_discovery_button");

        var text = $("#content_discovery_text");

        button.click(function () {
            text.html("Loading...");

            $.ajax({
                url : "discovery",
                dataType: "text",
                xhrFields: {
                    onprogress: function(e) {
                        var response = e.currentTarget.response;
                        text.html(response);
                    }
                },
                success : function (data) {
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    text.html("Status: " + textStatus);
                }
            });
        });
    }
};