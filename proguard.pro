-injars       dist/netacquireclient.jar
-outjars      dist/proguard.jar
-libraryjars  <java.home>/lib/rt.jar
-printmapping dist/proguard.map

-optimizationpasses 3
-overloadaggressively
-repackageclasses ''
-allowaccessmodification

-keep public class com.tlstyer.netacquireclient.Main {
    public static void main(java.lang.String[]);
}
