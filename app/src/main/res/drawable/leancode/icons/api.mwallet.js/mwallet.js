/**
 * Class of mwallet api
 */
class Mwallet {

    /**
     * language api
     * @type {string}
     */
    lang = 'ru';

    /**
     * version api
     * @type {string}
     */
    version = '1005';

    /**
     * url for api
     * @type {string}
     */
    api_url = '';

    /**
     * site id for api
     * @type {string}
     */
    api_sid = '';

    /**
     * password for create hash in request (sign)
     * just for example! Your password must be private.
     * @type {string}
     */
    api_passw = '';

    /**
     *
     * @param api_url
     * @param api_sid
     * @param api_passw
     */
    constructor(api_url, api_sid, api_passw) {
        this.api_url = api_url;
        this.api_sid = api_sid;
        this.api_passw = api_passw;
    }

    /**
     * Create and return hash from object data
     * @param data
     * @returns {*}
     */
    hash(data) {
       return CryptoJS.HmacMD5(JSON.stringify(data), this.api_passw).toString();
    }

    /**
     * Return current microtime
     * @param get_as_float
     * @returns {*}
     */
    microtime(get_as_float) {
        var now = new Date().getTime() / 1000;
        var s = parseInt(now, 10);

        return (get_as_float) ? now : (Math.round((now - s) * 1000) / 1000) + ' ' + s;
    }

    /**
     * Create
     * @param request
     */
    request(request) {

        var obj = {
            "cmd": request.cmd,
            "version": this.version,
            "sid": this.api_sid,
            "mktime": this.microtime(true).toString().replace(".", ""),
            "lang": this.lang,
            "data": request.data
        };

        obj.hash = this.hash(obj);

        var json = JSON.stringify(obj, undefined, 2);

        return $.ajax({
            url: this.api_url,
            async: false,
            type: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: json
        }).responseJSON;

    }
}