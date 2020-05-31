![Offline](https://i.imgur.com/Ydf9oKl.png?2)
Offline is a android library,which is used to indicate the user is turned off internet or turned on airplane mode. This library can be used with few lines of code and saves your precious time
# Demo

# Setup
##### 1. Provide the gradle dependency
Add it in your root build.gradle at the end of repositories:
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Add the gradle dependency to your `app` module `build.gradle` file:

```
	dependencies {
	        implementation 'com.github.ajithvgiri:offline:v1.0'
	}

```

##### 2. Initialization

##### Kotlin
add below code in your activity
``` kotlin
    var noInternetConnectionSnackBar: NoInternetConnectionSnackBar? = null
    override fun onResume() {
        super.onResume()
        noInternetConnectionSnackBar = NoInternetConnectionBuilder(applicationContext, findViewById(android.R.id.content)).build()
    }

    override fun onDestroy() {
        super.onDestroy()
        noInternetConnectionSnackBar?.destroy()
    }
```

#### Java
add below code in your activity
``` java
      NoInternetConnectionSnackBar noInternetConnectionSnackBar;
      @Override
    protected void onResume() {
        super.onResume();
        noInternetConnectionSnackBar = new NoInternetConnectionBuilder(getApplicationContext(), findViewById(android.R.id.content)).build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetConnectionSnackBar.destroy();
    }
```
### Screenshots
![Screenshots](https://i.imgur.com/MrMXc7d.jpg)

# Credits
This library is inspired by [Oops No Internet](https://github.com/ImaginativeShohag/Oops-No-Internet).
Library name credit goes to Md. Mahmudul Hasan Shohag.

License
----
Copyright 2020 Ajith v

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.