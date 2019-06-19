using UnityEngine;
using System.Collections;
using System;
using System.Threading;
using System.Collections.Generic;
using System.ComponentModel;
using System.IO.Ports;
using System.Text.RegularExpressions;
using System.Text;



public class T : MonoBehaviour
{
    private SerialPort sp;
    public GameObject Arm;
    
    // Start is called before the first frame update
    void Start()
    {
        Arm=GameObject.Find("EthanRightArm");
        Arm.transform.eulerAngles= new Vector3(0, 0, 220);
        transform.eulerAngles = new Vector3(-30,-25,-90);//Pitch Yaw Roll
        //串口初始化
        sp = new SerialPort("COM3", 115200, Parity.None, 8, StopBits.One);
        if (!sp.IsOpen)
        {
            sp.Open();
        }
        if (sp.IsOpen)
        {
            Debug.Log("YES");
        }
    }

    // Update is called once per frame
    void Update()
    {
        int x1 = 0;
        int y1 = 0;
        int z1 = 0;
        int x2 = 0;
        int y2 = 0;
        int z2 = 0;

        while (sp.ReadByte() == '#')
        {
            //Get x(pitch)
            x1 = (sp.ReadByte() - '0') * 100 + x1;
            x1 = (sp.ReadByte() - '0') * 10 + x1;
            x1 = (sp.ReadByte() - '0') + x1;
            //Get y(roll)
            y1 = (sp.ReadByte() - '0') * 100 + y1;
            y1 = (sp.ReadByte() - '0') * 10 + y1;
            y1 = (sp.ReadByte() - '0') + y1;
            //Get z(yaw)
            z1= (sp.ReadByte() - '0') * 100 + z1;
            z1 = (sp.ReadByte() - '0') * 10 + z1;
            z1 = (sp.ReadByte() - '0') + z1;
            transform.eulerAngles = new Vector3(-x1-30, z1-25, -y1-90);

            
            x2 = (sp.ReadByte() - '0') * 100 + x2;
            x2 = (sp.ReadByte() - '0') * 10 + x2;
            x2 = (sp.ReadByte() - '0') + x2;
            //Get y(roll)
            y2 = (sp.ReadByte() - '0') * 100 + y2;
            y2 = (sp.ReadByte() - '0') * 10 + y2;
            y2 = (sp.ReadByte() - '0') + y2;
            //Get z(yaw)
            z2 = (sp.ReadByte() - '0') * 100 + z2;
            z2 = (sp.ReadByte() - '0') * 10 + z2;
            z2 = (sp.ReadByte() - '0') + z2;
            Arm.transform.eulerAngles = new Vector3(-x2, z2, -y2 + 90);
           
            break;
        }

        
   
        
    }
}
