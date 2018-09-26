 function  | The name of the configuration |  type | value 
 ------------- | ------------- | ------------- | ------------- 
Enable atlas , true/false  | atlas.atlasEnabled | boolean  | true
bundlePackageId defines files that are not defined automatically  | atlas.tBuildConfig.packageIdFile | File  | 
Remote bundle list, artifactId  | atlas.tBuildConfig.outOfApkBundles | Set  | []
The value is packageName  | atlas.tBuildConfig.autoStartBundles | List  | 
To implement the PreLaunch class, multiple classes are used , No separate  | atlas.tBuildConfig.preLaunch | String  | 
atlasThe primary dex subcontracting mechanism, the first dex to set up only the startup code corresponding to atlas  | atlas.tBuildConfig.atlasMultiDex | boolean  | false
List of so files to delete  | atlas.tBuildConfig.removeSoFiles | Set  | []
Automatically generate the bundle's packageId  | atlas.tBuildConfig.autoPackageId | boolean  | true
Automatic allocation of packageId The minimum value  | atlas.tBuildConfig.minPackageId | int  | 35
If atlas is turned on, it must be true  | atlas.tBuildConfig.preProcessManifest | Boolean  | true
Merge the bundle jarResource file  | atlas.tBuildConfig.mergeAwbJavaRes | Boolean  | false
If atlas is turned on, it must be true  | atlas.tBuildConfig.useCustomAapt | Boolean  | true
The recommended value is set to false, You can reduce the amount of patch packages that are dynamically deployed  | atlas.tBuildConfig.aaptConstantId | Boolean  | true
Inject the core bundle information  | atlas.tBuildConfig.classInject | Boolean  | false
The old version of the main dex dynamic deployment, has been deprecated  | atlas.tBuildConfig.doPreverify | Boolean  | false
classInjection before proguard  | atlas.tBuildConfig.injectBeforeProguard | Boolean  | false
You need to do a databinding bundle, A value of packageName   | atlas.tBuildConfig.dataBindingBundles | Set  | []
proguardDo you need to read the confusing configuration in the bundle  | atlas.tBuildConfig.bundleProguardConfigEnabled | boolean  | true
The confusion in dependencies reads only the keep rule  | atlas.tBuildConfig.libraryProguardKeepOnly | boolean  | true
group:name,group2:name2  | atlas.tBuildConfig.bundleProguardConfigBlackList | Set  | []
Whether to use fast proguard  | atlas.tBuildConfig.fastProguard | boolean  | false
Whether to use the remote proguard cache  | atlas.tBuildConfig.proguardNetworkCacheEnabled | boolean  | false
Whether to use the proguard cache  | atlas.tBuildConfig.proguardCacheEnabled | boolean  | true
Whether to use the dex cache  | atlas.tBuildConfig.dexCacheEnabled | boolean  | true
Whether to use the remote dex cache  | atlas.tBuildConfig.dexNetworkCacheEnabled | boolean  | false
put awb so in lib/armeabi , not assets dir  | atlas.tBuildConfig.keepInLibSoNames | Set  | []
Build the baseline package, and the suggestion is open, otherwise the patch package will not be able to proceed  | atlas.tBuildConfig.createAP | Boolean  | true
Whether to rely on conflict to terminate packaging  | atlas.tBuildConfig.abortIfDependencyConflict | boolean  | false
Whether class conflicts terminate packaging  | atlas.tBuildConfig.abortIfClassConflict | boolean  | false
Such as: com.taobao.android:taobao-android-release:6.3.0-SNAPSHOT@ap   | atlas.buildTypes.debug.baseApDependency | String  | 
Such as: com.taobao.android:taobao-android-release:6.3.0-SNAPSHOT@ap   | atlas.buildTypes.release.baseApDependency | String  | 
Keep a list of launch launches  | atlas.manifestOptions.retainLaunches | Set  | []
Reserved permissions list  | atlas.manifestOptions.retainPermissions | Set  | []
Access list  | atlas.manifestOptions.permissionListFile | File  | 
Access list  | atlas.manifestOptions.permissionJsonFile | File  | 
The name of the system permissions removed  | atlas.manifestOptions.removeSystemPermissions | Set  | []
The name of the removed custom permission  | atlas.manifestOptions.removeCustomPermission | boolean  | false
Components add the coordinates of the bundle  | atlas.manifestOptions.addBundleLocation | boolean  | true
The ability to open new components  | atlas.manifestOptions.addAtlasProxyComponents | boolean  | false
A channel list that does not perform atlas added components  | atlas.manifestOptions.atlasProxySkipChannels | Set  | [:dex2oat, :dexmerge]
Use the atlas application, including atlasBasic initialization and multidex logic, Atlas must be opened  | atlas.manifestOptions.replaceApplication | boolean  | true
Using atlas's multiDex function, Atlas must be opened  | atlas.manifestOptions.addMultiDexMetaData | boolean  | true
Remove all providers  | atlas.manifestOptions.removeProvider | boolean  | false
Non-participating in the manifest merge dependent coordinates, group:name,group2:name2  | atlas.manifestOptions.notMergedBundles | Set  | []
Enable atlas , true/false  | atlas.multiDexConfigs.debug.fastMultiDex | boolean  | false
0unlimited  | atlas.multiDexConfigs.debug.dexCount | int  | 0
a,b;c,d  | atlas.multiDexConfigs.debug.dexSplitRules | String  | 
a  | atlas.multiDexConfigs.debug.mainDexBlackList | Set  | []
The custom needs to be placed in the entry class in the first dex  | atlas.multiDexConfigs.debug.firstDexClasses | Set  | []
Enable atlas , true/false  | atlas.multiDexConfigs.release.fastMultiDex | boolean  | false
0unlimited  | atlas.multiDexConfigs.release.dexCount | int  | 0
a,b;c,d  | atlas.multiDexConfigs.release.dexSplitRules | String  | 
a  | atlas.multiDexConfigs.release.mainDexBlackList | Set  | []
The custom needs to be placed in the entry class in the first dex  | atlas.multiDexConfigs.release.firstDexClasses | Set  | []
appThe application name of the registered signature  | atlas.patchConfigs.debug.appSignName | String  | 
 Play andfix patch bag   | atlas.patchConfigs.debug.createAPatch | boolean  | false
 Dynamic deployment patch bag   | atlas.patchConfigs.debug.createTPatch | boolean  | false
Dynamic deployment does not contain a list of files  | atlas.patchConfigs.debug.tPatchNotIncludeFiles | Set  | []
 andfix Package filtering class A list of files   | atlas.patchConfigs.debug.filterFile | File  | 
 andfix Package filtering class tabulation   | atlas.patchConfigs.debug.filterClasses | Set  | []
appThe application name of the registered signature  | atlas.patchConfigs.release.appSignName | String  | 
 Play andfix patch bag   | atlas.patchConfigs.release.createAPatch | boolean  | false
 Dynamic deployment patch bag   | atlas.patchConfigs.release.createTPatch | boolean  | false
Dynamic deployment does not contain a list of files  | atlas.patchConfigs.release.tPatchNotIncludeFiles | Set  | []
 andfix Package filtering class A list of files   | atlas.patchConfigs.release.filterFile | File  | 
 andfix Package filtering class tabulation   | atlas.patchConfigs.release.filterClasses | Set  | []
