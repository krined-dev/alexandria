classDiagram
direction BT
class AlexandriaCode {
<<Interface>>
   String description
   String code
   String version
}
class Code {
  + Code(String) 
   String value
}
class CodeRequest {
  + CodeRequest(CodeTypes, String, String) 
  + toString() String
  + copy-ei8oqFs(CodeTypes, String, String) CodeRequest
  + component1() CodeTypes
  + hashCode() Int
  + component3-dukSuBs() String
  + equals(Object?) Boolean
  + component2-c1ChJ0Y() String
   String version
   CodeTypes codeType
   String fromRegistry
}
class CodeTypes {
<<enumeration>>
  - CodeTypes() 
  + values() CodeTypes[]
  + valueOf(String) CodeTypes
}
class ExternalDsHttpClient {
<<Interface>>
  + retrieveDataFromExternal(Source, ExternalHttpRequest) ExternalHttpResponse
}
class ICD10 {
  + ICD10(String, String, String) 
  + component1-d5wHzb0() String
  + component2() String
  + equals(Object?) Boolean
  + toString() String
  + copy-9xcLQWY(String, String, String) ICD10
  + component3-c1ChJ0Y() String
  + hashCode() Int
   String description
   String code
   String version
}
class InternalHttpClient {
<<Interface>>
  + sendUpdatedDataNotification(CodeTypes) HttpRequest
}
class NCMP {
  + NCMP(String, String, String) 
  + component1-d5wHzb0() String
  + component2() String
  + equals(Object?) Boolean
  + hashCode() Int
  + component3-c1ChJ0Y() String
  + copy-9xcLQWY(String, String, String) NCMP
  + toString() String
   String description
   String code
   String version
}
class NCSP {
  + NCSP(String, String, String) 
  + copy-9xcLQWY(String, String, String) NCSP
  + toString() String
  + hashCode() Int
  + component3-c1ChJ0Y() String
  + equals(Object?) Boolean
  + component1-d5wHzb0() String
  + component2() String
   String description
   String code
   String version
}
class RefreshService {
<<Interface>>
  + runService() Unit
}
class RegistryName {
  + RegistryName(String) 
   String value
}
class Source {
<<Interface>>
   CodeTypes code
   String name
   String url
}
class StorageService {
<<Interface>>
  + updateCodes(List~AlexandriaCode~) Unit
  + storeCodes(List~AlexandriaCode~) Unit
  + retrieveCodes(CodeTypes, String) List~AlexandriaCode~
}
class Version {
  + Version(String) 
   String value
}
class data  CodeResponse {
  + CodeResponse(List~AlexandriaCode~) 
   List~AlexandriaCode~ codes
}
class data  ExternalHttpRequest {
  + ExternalHttpRequest(String) 
   String request
}
class data  ExternalHttpResponse {
  + ExternalHttpResponse(String) 
   String response
}
class data  PostCode {
  + PostCode(String, String, String, String) 
   String city
   String municipality
   String number
   String county
}

CodeRequest "1" *--> "codeType 1" CodeTypes 
ICD10  ..>  AlexandriaCode 
NCMP  ..>  AlexandriaCode 
NCSP  ..>  AlexandriaCode 
data  CodeResponse "1" *--> "codes *" AlexandriaCode 
