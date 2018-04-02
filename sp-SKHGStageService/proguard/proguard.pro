-libraryjars  <java.home>/lib/rt.jar

#-keep class com.ecity.**{*;}
-keep class com.esri.schemas.arcgis.** {*;}
-keep class mapgis.SvrLib.** {*;}
-keep class org.codehaus.jettison.** {*;}
-keep class org.apache.cxf.** {*;}
-keep class schemas.** {*;}
-keep class com.ecity.se.core.** {*;}
-keep class com.ecity.se.factory.** {*;}
-keep class com.ecity.se.Interceptor.** {*;}
-keep class com.ecity.se.Serializable.** {*;}
-keep class com.ecity.se.token.** {*;}
-keep class org.apache.** {*;}
-keep class javax.ws.** {*;}
-keep class org.springframework.** {*;}
-keep class javax.servlet.** {*;}
-keep class com.ecity.se.cluster.** {*;}

-keep class com.ecity.**{
	public protected *;
}

-keep class com.ecity.server.response.**{
	public protected *;
}

-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

# -keep public class com.ecity.server.response.IdentifyResponse$* {
#    public <fields>;
#    public <methods>;
# }

-keep public class * implements java.io.Serializable{
	public protected private *; 
}