function require(script) {
    if (!JS_COMPILED) {
        document.write('\x3Cscript type="text/javascript" src="' + script + '">\x3C/script>');
    }
}
