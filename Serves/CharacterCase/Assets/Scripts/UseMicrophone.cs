using UnityEngine;
using UnityEngine.UI;
using System.IO;
using System;
using System.Collections.Generic;
using System.Collections;

[RequireComponent(typeof(AudioSource))]
public class UseMicrophone : MonoBehaviour
{   
    private AudioSource aud;
    public Text txt;
    public Button Startbtn;
    public Button Endbtn;
    public Button Playbtn;
    private bool isHaveMicrophone = false;
    private string[] devices;

    private string filePath = null;
    private AudioSource m_audioSource;
    private AudioClip m_audioClip;
    public const int SamplingRate = 8000;
    private const int HEADER_SIZE = 44;
    public bool isRecording = false;
    public Byte[] speech_Byte;
    private DateTime time = DateTime.Now;

    IEnumerator IEHttpGet(string filePath)
    {
        WWW www = new WWW("localhost:8080/SteelPipe/appFront/toneAnalyzer.do?filepath=" + filePath);
        yield return www;

        if (www.error != null)
        {
            print(www.error);
        }
        print(www.text);
    }

    // Use this for initialization
    void Start()
    {
        m_audioSource = GetComponent<AudioSource>();
        filePath = "D:/Project/Eclipse_EE_WorkSpace/SteelPipe/WebContent/static/hackathon/";

        aud = this.GetComponent<AudioSource>();
        Startbtn.onClick.AddListener(OnClickStartBtn);
        Endbtn.onClick.AddListener(OnClickEndBtn);
        Playbtn.onClick.AddListener(OnClickPlayBtn);
        
        //获取麦克风设备，判断设备是否有麦克风
        devices = Microphone.devices;
        if (devices.Length > 0)
        {
            isHaveMicrophone = true;
            txt.text = "设备有麦克风:" + devices[0];
        }
        else
        {
            isHaveMicrophone = false;
            txt.text = "设备没有麦克风";
        }
    }

    public void StartRecording()
    {
        isRecording = !isRecording;

        if (isRecording)
        {
            Microphone.End(null);
            foreach (string d in Microphone.devices)
            {
                Debug.Log("Devid :" + d);
            }

            m_audioClip = Microphone.Start(null, false, 60, SamplingRate);
        }
        else
        {
            int audioLength = 0;
            int lastPos = Microphone.GetPosition(null);

            if (Microphone.IsRecording(null))
            {
                audioLength = lastPos / SamplingRate;
            }
            else
            {
                audioLength = 1;
                Debug.Log("error : 录音时间太短");
            }
            Microphone.End(null);

            if (audioLength <= 1.0f) return;

            bool Res = SaveWav("test", m_audioClip);

        }
    }
    public void PlayAudioClip()
    {
        if (m_audioClip.length > 5 && m_audioClip != null)
        {
            if (m_audioSource.isPlaying)
            {
                m_audioSource.Stop();
            }
            Debug.Log("Channel :" + m_audioClip.channels + " ;Samle :" + m_audioClip.samples + " ;frequency :" + m_audioClip.frequency + " ;length :" + m_audioClip.length);
            m_audioSource.clip = m_audioClip;
            m_audioSource.Play();
        }
    }

    bool SaveWav(string filename, AudioClip clip)
    {
        try
        {
            if (!filename.ToLower().EndsWith(".wav"))
            {
                filename += ".wav";
            }

            filePath = filePath + filename;

            Debug.Log("Record Ok :" + filePath);

            Directory.CreateDirectory(Path.GetDirectoryName(filePath));

            using (FileStream fileStream = CreateEmpty(filePath))
            {
                ConvertAndWrite(fileStream, clip);
            }
            return true;
        }
        catch (Exception ex)
        {
            Debug.Log("error : " + ex);
            return false;
        }

    }

    FileStream CreateEmpty(string filePath)
    {
        FileStream fileStream = new FileStream(filePath, FileMode.Create);
        byte emptyByte = new byte();

        for (int i = 0; i < HEADER_SIZE; i++)
        {
            fileStream.WriteByte(emptyByte);
        }
        return fileStream;
    }

    void ConvertAndWrite(FileStream fileStream, AudioClip clip)
    {
        float[] samples = new float[clip.samples];

        clip.GetData(samples, 0);

        Int16[] intData = new Int16[samples.Length];

        Byte[] bytesData = new Byte[samples.Length * 2];

        int rescaleFactor = 32767; //to convert float to Int16

        for (int i = 0; i < samples.Length; i++)
        {
            intData[i] = (short)(samples[i] * rescaleFactor);

            Byte[] byteArr = new Byte[2];
            byteArr = BitConverter.GetBytes(intData[i]);
            byteArr.CopyTo(bytesData, i * 2);
        }

        speech_Byte = bytesData;

        fileStream.Write(bytesData, 0, bytesData.Length);

        WriteHeader(fileStream, clip);
    }

    void WriteHeader(FileStream fileStream, AudioClip clip)
    {

        int hz = clip.frequency;
        int channels = clip.channels;
        int samples = clip.samples;

        fileStream.Seek(0, SeekOrigin.Begin);

        Byte[] riff = System.Text.Encoding.UTF8.GetBytes("RIFF");
        fileStream.Write(riff, 0, 4);

        Byte[] chunkSize = BitConverter.GetBytes(fileStream.Length - 8);
        fileStream.Write(chunkSize, 0, 4);

        Byte[] wave = System.Text.Encoding.UTF8.GetBytes("WAVE");
        fileStream.Write(wave, 0, 4);

        Byte[] fmt = System.Text.Encoding.UTF8.GetBytes("fmt ");
        fileStream.Write(fmt, 0, 4);

        Byte[] subChunk1 = BitConverter.GetBytes(16);
        fileStream.Write(subChunk1, 0, 4);

        UInt16 two = 2;
        UInt16 one = 1;

        Byte[] audioFormat = BitConverter.GetBytes(one);
        fileStream.Write(audioFormat, 0, 2);

        Byte[] numChannels = BitConverter.GetBytes(channels);
        fileStream.Write(numChannels, 0, 2);

        Byte[] sampleRate = BitConverter.GetBytes(hz);
        fileStream.Write(sampleRate, 0, 4);

        Byte[] byteRate = BitConverter.GetBytes(hz * channels * 2); // sampleRate * bytesPerSample*number of channels, here 44100*2*2  
        fileStream.Write(byteRate, 0, 4);

        UInt16 blockAlign = (ushort)(channels * 2);
        fileStream.Write(BitConverter.GetBytes(blockAlign), 0, 2);

        UInt16 bps = 16;
        Byte[] bitsPerSample = BitConverter.GetBytes(bps);
        fileStream.Write(bitsPerSample, 0, 2);

        Byte[] datastring = System.Text.Encoding.UTF8.GetBytes("data");
        fileStream.Write(datastring, 0, 4);

        Byte[] subChunk2 = BitConverter.GetBytes(samples * 2 * channels);
        fileStream.Write(subChunk2, 0, 4);

        fileStream.Close();
        Debug.Log(" OK ");
    }


    private void OnClickStartBtn()
    {
        if ((isHaveMicrophone == false) || (Microphone.IsRecording(devices[0])))
        {
            return;
        }

        //开始录音
        aud.clip = Microphone.Start(devices[0], true, 20, 44100);
        // StartRecording();
    }

    private void OnClickEndBtn()
    {
        if ((isHaveMicrophone == false) || (!Microphone.IsRecording(devices[0])))
        {
            return;
        }

        //结束录音
        Microphone.End(devices[0]);
        
        bool Res = SaveWav(time.Hour + "-" + time.Minute + "-" + time.Second, aud.clip);
        StartCoroutine(IEHttpGet(time.Hour + "-" + time.Minute + "-" + time.Second));
        // StartRecording();
    }

    private void OnClickPlayBtn()
    {
        if ((isHaveMicrophone == false) || (Microphone.IsRecording(devices[0]) || aud.clip == null))
        {
            return;
        }

        //播放录音
        aud.Play();
        // PlayAudioClip();
    }
}