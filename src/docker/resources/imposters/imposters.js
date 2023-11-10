{
 "imposters": [
	{
	    "protocol": "grpc",
	    "port": 2526,
	    "loglevel": "debug",
	    "recordRequests": true,
	    "_note_services": "stripe grpc service",
	    "services": {
	        "oyster.rpc.payment.stripe.PaymentIntentRpc": {
	            "file": "src/payment/stripe/paymen_intent_rpc.proto"
	        }
	    },
	    "options": {
	        "protobufjs": {
	            "includeDirs": ["/proto.stripe", "/proto.stripe/src/commons/", "/proto.stripe/src/domain/", "/proto.stripe/src/payment/stripe/"]
	        }
	    },
	    "stubs": [
			{"predicates": [
					{
						"matches": { "path": "createPaymentIntent" },
						"caseSensitive": false
					}
				],
				"responses": [
					{
						"is": {
							"value": {
								"error": false,
								"msg": "",
								"success": true,
								"stripePaymentId": "ce47e482-886c-452d-98fc-655cc71ecacb",
								"paymentTransactionId": "77cce657-5701-494b-95ed-653c932e6558",
								"status": "requires_payment_method",
								"clientSecret": "my_client_secret",
								"amount": 2050
							}
						}
					}
				]
			}
			],
	    "callbackURLTemplate":"http://localhost:2525/imposters/:port/_requests"
	}
  ]
}