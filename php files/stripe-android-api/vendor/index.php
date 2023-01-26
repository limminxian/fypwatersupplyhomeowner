<?php
require 'D:/XAMPP/htdocs/stripe-android-api/vendor/autoload.php';

if (!empty($_POST['name']) &&
    !empty($_POST['addressLine']) &&
    !empty($_POST['postalCode']) &&
    !empty($_POST['amount']) &&
    !empty($_POST['description']) 
) {

  $name = $_POST['name'];
  $addressLine = $_POST['addressLine'];
  $postalCode = $_POST['postalCode'];
  $amount = $_POST['amount'];
  $description = $_POST['description'];

  $stripe = new \Stripe\StripeClient('sk_test_51MU2BTAbT012j7egI2342c9y1PPlKoXh48EU7oEs1qCQP5bZHdfSsRdeHJ0aYdIpCRmvA1SzPRyg5aXnkExj5Wyd00x6vNRM35'); //Secret key from Stripe API Keys

  $customer = $stripe->customers->create(
    [
      'name' => $name,
      'address' => [
        'line1' => $addressLine,
        'postal_code' => $postalCode,
        'city' => 'Singapore',
        'state' => 'Singapore',
        'country' => 'Singapore'
      ]
    ]
  );
  $ephemeralKey = $stripe->ephemeralKeys->create([
    'customer' => $customer->id,
  ], [
      'stripe_version' => '2022-08-01',
    ]);
  $paymentIntent = $stripe->paymentIntents->create([
    'amount' => $amount * 100,
    'currency' => 'sgd',
    'description' => $description,
    'customer' => $customer->id,
    'automatic_payment_methods' => [
      'enabled' => 'true',
    ],
  ]);

  echo json_encode(
    [
      'paymentIntent' => $paymentIntent->client_secret,
      'ephemeralKey' => $ephemeralKey->secret,
      'customer' => $customer->id,
      'publishableKey' => 'pk_test_51MU2BTAbT012j7egqDUyy2M8euzVpZG6VQa7l3m053mBYwjSfW7Tzp4edbt7vVZmJApva4LcnMQLaYxWXm7qUzTh006EhTdAJT' //Publishable key from Stripe API Keys
    ]
  );
  http_response_code(200); //impt to set response code to 200
}
