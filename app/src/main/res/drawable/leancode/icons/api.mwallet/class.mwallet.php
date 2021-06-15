<?php
/**
 * PHP класс для работы с сервисом Mobipay.ua
 * Для работы с классом нужно расширение cURL для отправки запросов
 *
 * 11.04.2017
 *
 * @author MobiPay.ua
 * @mail support@mobipay.ua
 * @link https://sandbox.mobipay.ua
 *
 */
class c_mwallet {

	/**
     * @var string идентификатор сайта
     */
	protected static $site_id   = "";

	/**
     * @var string пароль сайта
     */
	protected static $secretkey = "";

	/**
     * @var boolean debugger
     */
	public static $debugger = false;

    /**
     * @var string домен сервера
     */
    protected static $server = "https://api2.mobipay.ua";

	/**
     * @var string url куда будут отправляться все запросы
     */
    protected static $url    = "/api/json/json.php";

	/**
     * @var int версия API mobipay.ua
     */
	protected static $version   = 1005;

	/**
     * @var string язык интерфейса
     */
	private static $lang        = "en";

	/**
     * @var array данные для отправки на сервер mobipay.ua
     */
	private static $request     = array();

	/**
     * @var array внутрение ошибки
     */
	private static $error       = array(
		1 => 'No request for data',
		2 => 'Hash does not match',
		3 => 'An unknown error',
		4 => 'No Information about ordering',
		5 => 'Not all required fields',
		6 => 'Choose %s'
	);


	/**
     * установка языка интерфейса
     *
     * @param string $lang язык (en,ru,uk,fr,es)
     */
	public static function setLang($lang)
	{
		self::$lang = $lang;
	}

	/**
     * получение QR для регистрации/авторизации
     *
     * @param string $return_url куда вернуть ответ (otphash) после авторизации пользователя
     *
     * @return object
     * ->otp = One Time Password
     * ->qr  = url картинки для отображения QR кода
     * ->otpview = OTP для отображения пользователю вида 123-456
     * @return object при ошибке ->error = номер ошибки; ->desc =  описание ошибки
     *
     * @link https://sandbox.mobipay.ua/authqr/  описание функции
     *
     */
	public static function getOTP($return_url=null)
	{
		self::$request['cmd']  = "getOTP";
		self::$request['data'] = array(
			'return_url' => !empty($return_url) ? $return_url : null
		);
		return self::send_cmd();
	}

	/**
     * получаем otphash пользователя, который авторизовался по OTP
     *
     * @param string $otp OTP пароль
     *
     * @return object
     * ->success = true в случаи успеха
     * ->otphash = OTP HASH
     * @return object при ошибке ->error = номер ошибки; ->desc =  описание ошибки
     *
     * @link https://sandbox.mobipay.ua/authqr/#getOTPhash  описание функции
     *
     */
	public static function getOTPhash($otp)
	{
		if(!empty($otp))
		{
			self::$request['cmd']  = "getOTPhash";
			self::$request['data'] = array(
					'otp' => $otp
			);
			return self::send_cmd();
		}
		return false;
	}

	/**
     * получаем уникальный идентификатор пользователя, который авторизовался в этой сессии
     *
     * @param string $otphash OTP хеш
     *
     * @return string номер wp пользователя в случаи успеха
     * @return object при ошибке ->error = номер ошибки; ->desc =  описание ошибки
     *
     * @link https://sandbox.mobipay.ua/authqr/#authUser  описание функции
     *
     */
	public static function authUser($otphash=null)
	{
		$otphash = !empty($otphash) ? $otphash : (isset($_REQUEST['otphash']) && strlen($_REQUEST['otphash']) == 32 ? $_REQUEST['otphash'] : null);
		if(!empty($otphash))
		{
			self::$request['cmd']  = "authUser";
			self::$request['data'] = array(
					'otphash' => $otphash
			);
			return self::send_cmd();
		}
		return false;
	}

	/**
     * получаем аватару пользователя
     *
     * @param string $account_id идентификатор пользователя
     * @param array[width,height] $size размер аватары
     *
     * @return string url аватары пользователя в случаи успеха
     * @return object при ошибке ->error = номер ошибки; ->desc =  описание ошибки
     *
     * @link https://sandbox.mobipay.ua/authqr/#userPhoto  описание функции
     *
     */
	public static function userPhoto($account_id,$size)
	{
		self::$request['cmd']  = "userPhoto";
		self::$request['data'] = array(
				'account_id' => $account_id,
				'size' => is_array($size) && count($size) == 2 ? $size : null
		);
		return self::send_cmd();
	}

	/**
     * получаем информацию о пользователе
     *
     * @param string $account_id идентификатор пользователя
     *
     * @return object данных пользователя
     * @return object при ошибке ->error = номер ошибки; ->desc =  описание ошибки
     *
     * @link https://sandbox.mobipay.ua/authqr/#userInfo  описание функции
     *
     */
	public static function userInfo($account_id)
	{
		self::$request['cmd']  = "userInfo";
		self::$request['data'] = array(
				'account_id'    => $account_id
		);
		return self::send_cmd();
	}

	/**
     * выставляем счет для оплаты и получаем QR
     *
     * @param array $invoice данные выставленного счета и список продаваемых товаров/услуг
     *
     * @return object
     * ->invoice_id = ID счета
     * ->qr       = url QR кода
     * ->link_app = ссылка для открытия счета в приложении WP, если пользователь зашел на сайт с мобильного
     * @return object при ошибке ->error = номер ошибки; ->desc =  описание ошибки
     *
     * @link https://sandbox.mobipay.ua/payqr/  описание функции
     *
     */
	public static function createInvoice($invoice)
	{
		if(!is_array($invoice))
		{
			return (object) array(
				'error' => 4,
				'desc'  => self::$error[4]
			);
		}
		elseif(!isset($invoice['order_id']))
		{
			return (object) array(
				'error' => 6,
				'desc'  => sprintf(self::$error[6],'order_id')
			);
		}
		elseif(!isset($invoice['desc']))
		{
			return (object) array(
				'error' => 6,
				'desc'  => sprintf(self::$error[6],'desc')
			);
		}

		if(isset($invoice['products']) && is_array($invoice['products']))
		{
			foreach($invoice['products'] as $k => $v)
			{
				$invoice['products'][$k]['name']  = self::is_utf8($v['name'])  ? $v['name']  : self::change_encoding($v['name'],'UTF-8');
				$invoice['products'][$k]['image'] = self::is_utf8($v['image']) ? $v['image'] : self::change_encoding($v['image'],'UTF-8');
			}
		}

		if(isset($invoice['fields_app']) && is_array($invoice['fields_app']))
		{
			foreach($invoice['fields_app'] as $k => $v)
			{
				foreach($v['data'] as $k2 => $v2)
				{
					$invoice['fields_app'][$k]['data'][$k2]['text']  = self::is_utf8($v2['text'])  ? $v2['text']  : self::change_encoding($v2['text'],'UTF-8');
				}
			}
		}

		self::$request['cmd']  = "createInvoice";
		self::$request['data'] = array(
				'order_id'   	   => $invoice['order_id'],
				'desc'       	   => self::is_utf8($invoice['desc']) ? $invoice['desc'] : self::change_encoding($invoice['desc'],'UTF-8'),
				'amount'     	   => isset($invoice['amount']) && floatval($invoice['amount']) > 0 ? $invoice['amount'] : 0,
				'currency'   	   => isset($invoice['currency']) ? $invoice['currency'] : 'UAH',
				'test'       	   => isset($invoice['test']) ? $invoice['test'] : 0,
				'transtype'    	   => isset($invoice['transtype']) ? $invoice['transtype'] : 0,
				'products'   	   => isset($invoice['products']) && is_array($invoice['products']) ? $invoice['products'] : null,
				'user_to'    	   => isset($invoice['user_to']) ? $invoice['user_to'] : null,
				'date_life'  	   => isset($invoice['date_life']) ? $invoice['date_life'] : null,
				'date_start_push'  => isset($invoice['date_start_push']) ? $invoice['date_start_push'] : null,
				'count_push'  	   => isset($invoice['count_push']) ? $invoice['count_push'] : null,
				'result_url' 	   => isset($invoice['result_url']) ? urlencode($invoice['result_url']) : null,
				'success_url' 	   => isset($invoice['success_url']) ? urlencode($invoice['success_url']) : null,
				'fail_url' 	 	   => isset($invoice['fail_url']) ? urlencode($invoice['fail_url']) : null,
				'fields_app'	   => isset($invoice['fields_app']) && is_array($invoice['fields_app']) ? $invoice['fields_app'] : null,
				'fields_other'	   => isset($invoice['fields_other']) && is_array($invoice['fields_other']) ? $invoice['fields_other'] : null,
				'long_term'    	   => isset($invoice['long_term']) ? $invoice['long_term'] : 0
		);
		return self::send_cmd();
	}


    /**
     * Создаем QR данных
     *
     * @param array|object $data*
     * @param string $url_request
     * @param string $queueId
     *
     * @return object QR
     * @return object при ошибке ->error = номер ошибки; ->desc =  описание ошибки
     *
     * @link https://sandbox.mobipay.ua/payqr/#getQRData  описание функции
     *
     */
    public static function getQRData($data,$queueId=null,$url_request=null)
	{
		self::$request['cmd']  = "getQRData";
		self::$request['data'] = array(
				'request'     => is_array($data) || is_object($data) ? $data : null,
                'url_request' => $url_request,
                'queueId'     => $queueId
		);
		return self::send_cmd();
	}

	/**
     * получаем информацию о результате платежа по его токену
     *
     * @param string $invoice_id ID счета
     * @param int $mark маркер (только для долгосрочных QR кодов)
     * @param string $order_id номер заказа
     *
     * @return object статус платежа
     * @return object при ошибке ->error = номер ошибки; ->desc =  описание ошибки
     *
     * @link https://sandbox.mobipay.ua/payqr/#statusPayment  описание функции
     *
     */
	public static function statusPayment($_)
	{
		self::$request['cmd']  = "statusPayment";
		self::$request['data'] = array(
				'invoice_id' => isset($_['invoice_id']) ? $_['invoice_id'] : null,
				'mark'	     => isset($_['mark']) ? $_['mark'] : null,
                'order_id'   => isset($_['order_id']) ? $_['order_id'] : null
		);
		return self::send_cmd();
	}

	/**
     * обновить маркер оплаченного счета (только для долгосрочных QR кодов)
     *
     * @param string $trans_id ID транзакции
     * @param int $mark маркер
     *
     * @return object при ошибке ->error = номер ошибки; ->desc =  описание ошибки
     *
     * @link https://sandbox.mobipay.ua/payqr/#updateMark  описание функции
     *
     */
	public static function updateMark($trans_id,$mark=null)
	{
		self::$request['cmd']  = "updateMark";
		self::$request['data'] = array(
				'trans_id' => $trans_id,
				'mark'	   => $mark
		);
		return self::send_cmd();
	}

	/**
     * снять заблокированную сумму
     *
     * @param string $trans_id ID транзакции
     * @param float $amount сумма
     *
     * @return object статус платежа
     * @return object при ошибке ->error = номер ошибки; ->desc =  описание ошибки
     *
     * @link https://sandbox.mobipay.ua/payqr/#settlePayment  описание функции
     *
     */
	public static function settlePayment($trans_id,$amount)
	{
		self::$request['cmd']  = "settlePayment";
		self::$request['data'] = array(
				'trans_id' => $trans_id,
                'amount'   => $amount
		);
		return self::send_cmd();
	}

	/**
     * Возврат/Отмена платежа
     *
     * @param string $trans_id ID транзакции
     *
     * @return object статус платежа
     * @return object при ошибке ->error = номер ошибки; ->desc =  описание ошибки
     *
     * @link https://sandbox.mobipay.ua/payqr/#refundPayment  описание функции
     *
     */
	public static function refundPayment($trans_id,$amount)
	{
		self::$request['cmd']  = "refundPayment";
		self::$request['data'] = array(
				'trans_id' => $trans_id,
                'amount'   => $amount
		);
		return self::send_cmd();
	}

    /**
     * вывод денег на карты VISA & MasterCard
     *
     * @param string $account_id идентификатор пользователя
     * @param float $amount сумма
     *
     * @return object
     * @return object при ошибке ->error = номер ошибки; ->desc =  описание ошибки
     *
     * @link https://sandbox.mobipay.ua/payqr/#account2card  описание функции
     *
     */
	public static function account2card($account_id,$amount)
	{
		self::$request['cmd']  = "account2card";
		self::$request['data'] = array(
				'account_id' => $account_id,
				'amount' => $amount
		);
		return self::send_cmd();
	}

    /**
     * Пополнение мобильного телефона
     * @param mixed $mobile номер который пополнить
     * @param mixed $amount сумма пополнения
     *
     * @return object
     * @return object при ошибке ->error = номер ошибки; ->desc =  описание ошибки
     *
     * @link https://sandbox.mobipay.ua/payqr/#p2Phone  описание функции
     */
    public static function p2Phone($mobile,$amount)
	{
		self::$request['cmd']  = "p2Phone";
		self::$request['data'] = array(
				'mobile' => $mobile,
				'amount' => $amount
		);
		return self::send_cmd();
	}

	/**
     * создание токена для подписи запросов
     *
     * @link https://sandbox.mobipay.ua  описание функции
     *
     */
	private static function token()
	{
		self::$request['version'] = self::$version;
		self::$request['sid']     = self::$site_id;
		self::$request['mktime']  = self::getmicrotime();
		self::$request['lang']	  = self::$lang;
		self::$request['hash']    = hash_hmac("md5", json_encode(self::$request, JSON_UNESCAPED_UNICODE + JSON_UNESCAPED_SLASHES), self::$secretkey);
	}

	/**
     * получение и обработка статуса оплаты
     *
     * ВНИМАНИЕ! Используется только для сайтов (серверов) когда при выставлении счета указывается result_url
     *
     * @return object
     *
     * @link https://sandbox.mobipay.ua/payqr/#resultPay  описание функции
     *
     */
	public static function result_pay()
	{
        if(!is_null($respons = json_decode(file_get_contents("php://input"))))
        {
            $hash = hash_hmac('md5', $respons->trans_id.':::'.$respons->status_pay.':::'.self::$site_id.':::'.$respons->order_id.':::'.$respons->amount.':::'.$respons->currency.':::'.$respons->mktime.':::'.$respons->test, self::$secretkey);

            if($hash === $respons->hash)
            {
				return (object) array(
					'success'  => true,
					'status'   => $respons->status_pay,
					'order_id' => $respons->order_id,
                    'trans_id' => $respons->trans_id
				);
			}
            else
            {
				return (object) array(
					'error'    => 2,
					'desc'     => self::$error[2],
					'status'   => $respons->status_pay,
					'order_id' => $respons->order_id,
                    'trans_id' => $respons->trans_id
				);
			}
        }
        else
        {
            return (object) array(
			    'error'    => 5,
			    'desc'     => self::$error[5]
		        );
        }
	}


	/**
     * Если данные не в UTF-8, то перекодируем
     */
	private static function change_encoding($text, $encoding)
	{
		return mb_convert_encoding($text, $encoding, mb_detect_encoding($text));
	}

	/**
     * функция обнаружения того, что строка $str закодирвана UTF-8 (бинарно)
     *
     * @param string $str строка символов
     *
     * @return boolean true если UTF-8 или false если ASCII
     *
     */
    private static function is_utf8($str)
	{
		for($i = 0; $i < strlen($str); $i++)
		{
			if(ord($str[$i]) < 0x80) $n=0; # 0bbbbbbb
			elseif ((ord($str[$i]) & 0xE0) == 0xC0) $n=1; # 110bbbbb
			elseif ((ord($str[$i]) & 0xF0) == 0xE0) $n=2; # 1110bbbb
			elseif ((ord($str[$i]) & 0xF0) == 0xF0) $n=3; # 1111bbbb
			else return false; # Does not match any model
			for($j = 0; $j < $n; $j++)
			{ # n octets that match 10bbbbbb follow ?
				if((++$i == strlen($str)) || ((ord($str[$i]) & 0xC0) != 0x80)) return false;
			}
		}
		return true;
    }

	/**
     * Возвращает microtime
     *
     * @return string
     *
     */
	public static function getmicrotime()
	{
		list($usec, $sec) = explode(" ", substr(microtime(), 2));
		return substr($sec.$usec, 0, 15);
	}


	/**
     * отправка запроса и получение ответа с сервера WEB Passport
     *
     * @return object ответ с сервера
     * @return object при ошибке ->error = номер ошибки; ->desc =  описание ошибки
     *
     */
	private static function send_cmd()
	{
		if (!function_exists('curl_version')) {
            die('To work correctly, you need to install cURL library - https://php.net/curl');
        }

		if(!is_array(self::$request) || count(self::$request) == 0)
		{
			return (object) array(
				'error' => 1,
				'desc'  => self::$error[1]
			);
		}

		self::token(); // подпись запроса hash

        $json = json_encode(self::$request, JSON_UNESCAPED_UNICODE + JSON_UNESCAPED_SLASHES);

		if(self::$debugger)
		{
			echo '<pre>'; print_r(self::$request); echo '</pre>';
            echo '<pre>'; print_r(self::$request['data']); echo '</pre>';
			echo '<pre>'; echo $json; echo '</pre>';

            self::save_log('SEND = ' . $json);
		}

		$ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, self::$server . self::$url);
        curl_setopt($ch, CURLOPT_HEADER, 0);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $json);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json'));
        curl_setopt($ch, CURLOPT_TIMEOUT, 30);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 0);
    	$result = curl_exec($ch);

        if(self::$debugger)
        {
            self::save_log('GET = ' . $result);
        }

		self::$request = array(); // очищаем память

		if(curl_errno($ch) != 0)
		{
			$result = array(
				'error' => curl_errno($ch),
				'desc'  => curl_error($ch)
			);
			curl_close($ch);
			return (object) $result;
		}

		curl_close($ch);

		if(self::$debugger)
		{
			echo '<pre>'; var_dump($result); echo '</pre>';
		}

		$result = json_decode($result);

		if(self::$debugger)
		{
			echo '<pre>'; print_r($result); echo '</pre>';
		}

		if(is_object($result))
		{
			$hash1 = $result->hash; unset($result->hash);
			$hash2 = hash_hmac('md5', json_encode($result, JSON_UNESCAPED_UNICODE + JSON_UNESCAPED_SLASHES), self::$secretkey);
			if($hash1 === $hash2)
			{
				return $result->data;
			}
            else
			{
				return (object) array(
					'error' => 2,
					'desc'  => self::$error[2]
				);
			}
		}


		return (object) array(
			'error' => 3,
			'desc'  => self::$error[3]
		);
	}

    /**
     * Сохранение логов
     * @param string $data
     */
    private static function save_log($data=null)
    {
        $log  = "************************************************************************************\n";
        $log .= "Time:            ".date("H:i:s")." \n";
        $log .= "Method:          ".@$_SERVER['REQUEST_METHOD']." \n";
        $log .= "************************************************************************************\n";
        if(!empty($data))
        {
            $log .= "DATA: \n";
            $log .= $data."\n\n";
        }
        $log .= "GET: \n";
        $log .= json_encode($_GET,JSON_UNESCAPED_UNICODE + JSON_UNESCAPED_SLASHES)."\n\n";
        $log .= "POST: \n";
        $log .= json_encode($_POST,JSON_UNESCAPED_UNICODE + JSON_UNESCAPED_SLASHES)."\n\n";
        $log .= "INPUT POST: \n";
		$log .= file_get_contents("php://input")."\n";
        $log .= "*************************************************************************************\n\n";


        $dir = __DIR__.'/'. date("Y_m_d") .'/'. date("H");
        if(!is_dir($dir)) mkdir($dir,0755,true);
        $filename = $dir . '/mwallet.api.log';
        $fileopen = fopen($filename,'a');
                    fwrite($fileopen,$log);
                    fclose($fileopen);
    }
}
?>