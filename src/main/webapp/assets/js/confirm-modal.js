function confirmAction(message, callback) {

    if (confirm(message)) {
        callback();
    }

}