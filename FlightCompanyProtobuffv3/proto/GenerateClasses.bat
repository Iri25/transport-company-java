
echo "Generating Java classes"
protoc -I=. --java_out=javaFiles --csharp_out=csharpFiles FlightCompanyProtocolv3.proto
