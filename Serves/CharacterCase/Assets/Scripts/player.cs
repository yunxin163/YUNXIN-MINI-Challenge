using UnityEngine;
using UnityEngine.UI;
using System.IO;
using System;
using System.Collections.Generic;
using System.Collections;

[RequireComponent(typeof(AudioSource))]
public class player : MonoBehaviour {

    public Animator animator;
    public Rigidbody rigidBody;
    public GameObject human;

    private float inputH;
    private float inputV;
    private bool run;

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

    private Texture texture;
    public Text chatText;
    private string username = "Me";
    private string emotion = "(normal)";
    public InputField chatInput;
    public ScrollRect scrollRect;

    void Start()
    {
        animator = GetComponent<Animator>();
        rigidBody = GetComponent<Rigidbody>();
        run = false;

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

    void Update()
    {
        if(Input.GetKeyDown("1"))
        {
            StartCoroutine(IEHttpGet());
        }
        /*
        if (Input.GetKeyDown("1"))
        {
            // 动画名-Base Layer-从0s开始播放。
            animator.Play("WAIT01", -1, 0f);
        }
        if (Input.GetKeyDown("2"))
        {
            animator.Play("WAIT02", -1, 0f);
        }
        if (Input.GetKeyDown("3"))
        {
            animator.Play("WAIT03", -1, 0f);
        }
        if (Input.GetKeyDown("4"))
        {
            animator.Play("WAIT04", -1, 0f);
        }
        if (Input.GetKeyDown("5"))
        {
            animator.Play("HANDUP00_R", -1, 0f);
        }
        
        if (Input.GetMouseButtonDown(0))
        {
            int n = Random.Range(0, 2);

            if (n == 0)
            {
                animator.Play("DAMAGED00", -1, 0f);
            }
            else
            {
                animator.Play("DAMAGED01", -1, 0f);
            }
        }
        

        if (Input.GetKey(KeyCode.LeftShift))
        {
            run = true;
        }
        else
        {
            run = false;
        }

        if (Input.GetKey(KeyCode.Space))
        {
            animator.SetBool("jump", true);
        }
        else
        {
            animator.SetBool("jump", false);
        }

        inputH = Input.GetAxis("Horizontal");
        inputV = Input.GetAxis("Vertical");

        animator.SetFloat("inputH", inputH);
        animator.SetFloat("inputV", inputV);
        animator.SetBool("run", run);

        float moveX = inputH * 20f * Time.deltaTime;
        float moveZ = inputV * 50f * Time.deltaTime;

        if (moveZ <= 0)
        {
            moveX = 0f;
        }
        else if (run)
        {
            moveX *= 3f;
            moveZ *= 3f;
        }

        rigidBody.velocity = new Vector3(moveX, 0f, moveZ);
        */
    }

    IEnumerator IEHttpGet(string filePath)
    {
        WWW www = new WWW("localhost:8080/SteelPipe/appFront/toneAnalyzer.do?filepath=" + filePath);
        yield return www;

        if (www.error != null)
        {
            print(www.error);
        }
        print(www.text);
        if (www.text.Equals(""))
        {
            animator.Play("WAIT00", -1, 0f);
            ChangePic("neutral");
        }
        else if (www.text.Equals("anger"))
        {
            animator.Play("DAMAGED01", -1, 0f);
            ChangePic("angry");
        }
        else if (www.text.Equals("sadness"))
        {
            animator.Play("DAMAGED00", -1, 0f);
            ChangePic("sad");
        }
        else if (www.text.Equals("confident"))
        {
            animator.Play("WAIT03", -1, 0f);
            ChangePic("surprised");
        }
        else if (www.text.Equals("happy"))
        {
            animator.Play("WAIT04", -1, 0f);
            ChangePic("happy");
        }
        else {
            ChangePic("neutral");
        }
        Update();
    }

    IEnumerator IEHttpGet()
    {
        WWW www = new WWW("localhost:8080/SteelPipe/appFront/getMsg.do");
        yield return www;

        if (www.error != null)
        {
            print(www.error);
        }
        JsonAnalysis jsonAnalysis = JsonUtility.FromJson<JsonAnalysis>(www.text);
        if (jsonAnalysis.tone.Equals(""))
        {
            emotion = "(normal)";
            animator.Play("WAIT00", -1, 0f);
            ChangePic("neutral");
        }
        else if (jsonAnalysis.tone.Equals("anger"))
        {
            emotion = "(anger)";
            animator.Play("WAIT01", -1, 0f);
            ChangePic("angry");
        }
        else if (jsonAnalysis.tone.Equals("sadness"))
        {
            emotion = "(sadness)";
            animator.Play("WAIT02", -1, 0f);
            ChangePic("sad");
        }
        else if (jsonAnalysis.tone.Equals("confident"))
        {
            emotion = "(confident)";
            animator.Play("WAIT03", -1, 0f);
            ChangePic("suprised");
        }
        else
        {
            emotion = "(normal)";
            ChangePic("neutral");
        }
        string addText = "\n" + emotion + "<color=red>Other</color>:   " + jsonAnalysis.message;
        chatText.text += addText;
        chatInput.text = "";
        chatInput.ActivateInputField();
        Canvas.ForceUpdateCanvases();
        scrollRect.verticalNormalizedPosition = 0f;
        Canvas.ForceUpdateCanvases();
        emotion = "(normal)";
        Update();
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
    }

    private void OnClickPlayBtn()
    {
        if ((isHaveMicrophone == false) || (Microphone.IsRecording(devices[0]) || aud.clip == null))
        {
            return;
        }

        //播放录音
        aud.Play();
    }

    private void ChangePic(string id)
    {
        texture = Resources.Load<Texture>("Textures/" + id);
        human.GetComponent<Renderer>().material.mainTexture = texture;
    }

}
