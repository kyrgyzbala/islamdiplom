<?php
error_reporting(E_ALL);	ini_set('display_errors', 1);
header("Cache-Control: no-store, no-cache, must-revalidate, max-age=0");
header("Cache-Control: post-check=0, pre-check=0", false);
header("Pragma: no-cache");
header('Content-Type: text/html; charset=utf-8');
require_once('class.mwallet.php');
// установка языка интерфейса
c_mwallet::setLang('ru');

// Включить отладку
c_mwallet::$debugger = true;

echo '<pre>';
// ** получение QR для регистрации/авторизации
print_r(c_mwallet::getOTP());

// ** получаем otphash пользователя, который авторизовался по OTP
print_r(c_mwallet::getOTPhash("613098"));

// ** получаем номер WP пользователя, который авторизовался в этой сессии
print_r(c_mwallet::authUser('4635080e308ef9f0435466e57eb2d57f'));

// ** получаем аватару пользователя
print_r(c_mwallet::userPhoto("111111111",array(300,300)));

// ** получаем информацию о пользователе
print_r(c_mwallet::userInfo('111111111'));

// ** выставляем счет для оплаты и получаем QR
// * список продуктов (необязательно)
$products = [
    [
	    'id'       => '1',
	    'name'     => 'Trainz Classics Volume 3 DVD',
	    'amount'   => 100,
	    'count'    => 1,
	    'image'    => 'http://camelot.multilocal.ru/pic/games/TraCla.jpg'
    ]
];
// * массив дополнительных полей для выбора покупателя
$ar = [
	[
		'type' => 'select',
		'id'   => 'gift',
		'data' => [
			[
				'value' => '1',
				'text'  => 'Подарок так себе'
			],
			[
				'value' => '2',
				'text'  => 'Подарок дорогой'
			]
		]
	]
];
// * создаем счет
$invoice = array(
	'test' => 0,
	//'user_to' => '380935960444',
	//'date_life' => '2016-09-10',
	//'date_start_push' => '2016-07-08',
	//'count_push' => 1,
	'order_id'  => '1881028321',
	'amount'	=> 100,
	'currency'  => 'UAH',
	'desc'      => 'Покупка товаров согласно счета #6598741',
	'transtype' => 1,
	//'fields_app'=> $ar,
	'products'  => $products,
	'long_term' => 0,
    'result_url' => 'http://otp.mobipay.ua/api.php',
	'fields_other'=> [
		[
			'custom' => 1
		]
	]
);
print_r(c_mwallet::createInvoice($invoice));

// ** получаем информацию о результате платежа по его токену
print_r(c_mwallet::statusPayment([
    'invoice_id' => '986721482479',
    'mark' => null,
    'order_id' => null
  ]));

// ** обновить маркер оплаченного счета (только для долгосрочных QR кодов)
print_r(c_mwallet::updateMark("389479225401",2));

// ** снять заблокированную сумму
print_r(c_mwallet::settlePayment("389479225401",100));

// ** Возврат/Отмена платежа
print_r(c_mwallet::refundPayment("389479225401",100));


// ** вывести деньги на карту
print_r(c_mwallet::account2card('111111111',100));

// ** Пополнить мробильный телефон
print_r(c_mwallet::p2Phone('380980818305',500));
echo '</pre>';
?>
