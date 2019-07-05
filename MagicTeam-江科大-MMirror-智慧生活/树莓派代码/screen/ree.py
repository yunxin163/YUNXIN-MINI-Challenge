# -*- coding: utf-8 -*-
import paho.mqtt.client as mqtt
topic = "mytest"
def on_connect(client, userdata, flags, rc):
    print("Connected with result code "+str(rc))

    
    client.subscribe(topic, qos=1)


def on_message(client, userdata, msg):
    print(msg.topic+" "+str(msg.payload))
protocol="MQTTv31"
user = "mymind/mycry"
pwd = "As6pXLkzzq8qzNk22rorqcuGC/4uz8bHThsTRJWxZug="
client = mqtt.Client(
    client_id="test_mqtt_receiver_1",
    clean_session=True,
    userdata=None,
    
)

    
client.username_pw_set(user, pwd) 
client.on_connect = on_connect
client.on_message = on_message
print(client.on_message)

client.connect("mymind.mqtt.iot.bj.baidubce.com", 1883, 60)

# Blocking call that processes network traffic, dispatches callbacks and
# handles reconnecting.
# Other loop*() functions are available that give a threaded interface and a
# manual interface.
client.loop_forever()
