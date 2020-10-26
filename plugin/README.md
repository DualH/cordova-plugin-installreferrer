# cordova-plugin-installreferrer
<a href="https://badge.fury.io/js/install-referrer" target="_blank"><img height="21" style='border:0px;height:21px;' border='0' src="https://badge.fury.io/js/install-referrer.svg" alt="NPM Version"></a>
<a href='https://www.npmjs.org/package/install-referrer' target='_blank'><img height='21' style='border:0px;height:21px;' src='https://img.shields.io/npm/dt/install-referrer.svg?label=NPM+Downloads' border='0' alt='NPM Downloads' /></a>
<br><img src="https://nodei.co/npm/install-referrer.png"/>

This plugin is used for get the information sent from Play Store after install app

## Supported platforms

- Android

## Installation

- From cordova plugins registry: `cordova-plugin-installreferrer`
- From github repository: `cordova plugin add https://github.com/DualH/cordova-plugin-installreferrer.git`

## First of all

Add InstallReferrer to modules and providers


## Usage
```javascript
import { InstallReferrer } from 'install-referrer';

...

contructor(private _installReferrer: InstallReferrer) {
  this._installReferrer.getReferrer()
  .then(data => {
    // data is a array with all parameters received
  })
  .catch(err => {});
}
```

## Test

In console

```
$ adb shell
$ am broadcast -a com.android.vending.INSTALL_REFERRER -n <package-name>/cordova.plugin.installreferrer.Receiver --es referrer "test_referrer=test&test_referrer1=test"
```

# Credits

Currently maintained by [@DualH](https://github.com/DualH)

Created by [@DualH](https://github.com/DualH) - [Donate with PayPal](https://www.paypal.me/rosano97/10)
