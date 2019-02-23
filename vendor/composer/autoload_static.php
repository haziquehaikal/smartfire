<?php

// autoload_static.php @generated by Composer

namespace Composer\Autoload;

class ComposerStaticInit3aeee7442ba10af221471dc2cde9290a
{
    public static $prefixLengthsPsr4 = array (
        'T' => 
        array (
            'Twilio\\' => 7,
        ),
    );

    public static $prefixDirsPsr4 = array (
        'Twilio\\' => 
        array (
            0 => __DIR__ . '/..' . '/twilio/sdk/Twilio',
        ),
    );

    public static function getInitializer(ClassLoader $loader)
    {
        return \Closure::bind(function () use ($loader) {
            $loader->prefixLengthsPsr4 = ComposerStaticInit3aeee7442ba10af221471dc2cde9290a::$prefixLengthsPsr4;
            $loader->prefixDirsPsr4 = ComposerStaticInit3aeee7442ba10af221471dc2cde9290a::$prefixDirsPsr4;

        }, null, ClassLoader::class);
    }
}
