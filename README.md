# Ohai
Ohai Notification Library for android


Add dependencies

    compile 'com.google.android.gms:play-services-gcm:+'
    compile 'dev.dworks.libs:volleyplus:+'
    compile 'com.google.code.gson:gson:+'
    
And then add compile and add the generated .aar file as a new module.

Add,
    
    Ohai.getInstance(context).setEmail("").setName("")
    .setLocation("").setMobile("").start();
