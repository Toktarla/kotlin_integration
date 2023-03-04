import 'package:flutter/material.dart';
import 'package:flutter/services.dart';


class ToktarPage extends StatefulWidget {
  const ToktarPage({Key? key}) : super(key: key);

  @override
  State<ToktarPage> createState() => _ToktarPageState();
}

class _ToktarPageState extends State<ToktarPage> {
  static const batteryChannel = MethodChannel("com.toktarsultan/battery");
  static const platform = MethodChannel("com.toktarsultan/getMessage");

  String batteryLevel = "Waiting...";
  String message = "Unknown Message";




  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.end,
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Text(message),

            Text(batteryLevel,style: TextStyle(color: Colors.red,fontSize: 40),),
            ElevatedButton(
                onPressed: getBatteryLevel,
                child: Text("Get Battery Level")
            ),
          ElevatedButton(
              onPressed: getMessage,
              child: Text("Get Message")
          ),

        ],
      ),
    );

  }

  Future getBatteryLevel() async {
    final arguments = {"name":"Toktar Sultan"};
    final String newBatteryLevel = await batteryChannel.invokeMethod('getBatteryLevel',arguments);
    setState(() {
      batteryLevel = "$newBatteryLevel";
    });
  }
  Future<void> getMessage() async{
    String messageFromKotlin;
    try{
      messageFromKotlin = await batteryChannel.invokeMethod("getMessageFromKotlin");
    } on PlatformException catch(e){
      messageFromKotlin = "Failed to receive message";

    }
    setState(() {
      message = messageFromKotlin;
    });

  }
}
