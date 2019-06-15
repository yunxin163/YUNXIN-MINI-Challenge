using UnityEngine;
using System.Collections;
using System.Runtime.InteropServices;
using System.Reflection;
using System.Diagnostics;
using System;
using System.IO;
using UnityEngine.UI;

public class CameraTest : MonoBehaviour
{
    public WebCamTexture cameraTexture;
    public string cameraName = "";
    private bool isPlay = false;
    public GameObject human;
    private Texture texture;
    public Image img_Preview;

    void Start()
    {

    }

    // Update is called once per frame  
    void Update()
    {
        if (Input.GetKeyDown("2"))
        {
            StartCoroutine(OpenCamera());
        }
        if (Input.GetKeyDown("3"))
        {
            StartCoroutine(GetTexture());
        }
    }
    /// <summary>  
    /// 获取权限打开摄像头  
    /// </summary>  
    /// <returns></returns>  
    IEnumerator OpenCamera()
    {
        yield return Application.RequestUserAuthorization(UserAuthorization.WebCam);
        if (Application.HasUserAuthorization(UserAuthorization.WebCam))
        {
            WebCamDevice[] devices = WebCamTexture.devices;
            cameraName = devices[0].name;
            cameraTexture = new WebCamTexture(cameraName, 320, 240, 15);
            cameraTexture.Play();
            isPlay = true;
        }
    }

    public IEnumerator GetTexture()
    {
        cameraTexture.Pause();
        yield return new WaitForEndOfFrame();

        // 旋转预览精灵
        // int videoRotationAngle = cameraTexture.videoRotationAngle;
        // img_Preview.transform.parent.rotation = Quaternion.Euler(0, 0, -videoRotationAngle);

        // 确定预览图，要截取镜头的大小
        int w = cameraTexture.width;
        w = cameraTexture.height > w ? w : cameraTexture.height;
        Vector2 offset = new Vector2((cameraTexture.width - w) / 2, (cameraTexture.height - w) / 2);

        //Texture2D mTexture = new Texture2D(_webCamTexture.width, _webCamTexture.height, TextureFormat.ARGB32, false);
        //mTexture.SetPixels32(_webCamTexture.GetPixels32());
        Texture2D mTexture = new Texture2D(w, w, TextureFormat.ARGB32, false);
        mTexture.SetPixels(cameraTexture.GetPixels((int)offset.x, (int)offset.y, w, w));
        mTexture.Apply();

        // img_Preview.sprite = Sprite.Create(mTexture, new Rect(0, 0, w, w), Vector2.zero);
        // cameraTexture.Play();

        byte[] bt = mTexture.EncodeToPNG();
        string mPhotoName = DateTime.Now.ToString("yyyyMMddHHmmss") + ".png";
        string mPhotoPath = "D:/Project/Python_Project/StarGAN/StarGAN-master/stargan_celeba_256/samples/" + mPhotoName;
        System.IO.File.WriteAllBytes(mPhotoPath, bt);
        UnityEngine.Debug.LogError("Photo path:" + mPhotoPath);

        cameraTexture.Stop();
        isPlay = false;

        StartCoroutine(IEHttpGet(mPhotoPath));
    }

    void OnGUI()
    {
        if (isPlay)
        {
            GUI.DrawTexture(new Rect(0, 0, 320, 240), cameraTexture, ScaleMode.ScaleAndCrop);
        }

        if (GUI.Button(new Rect(20, 650, 100, 35), "ChooseFile"))
        {
            OpenFileName ofn = new OpenFileName();
            ofn.structSize = Marshal.SizeOf(ofn);
            ofn.filter = "All Files\0*.*\0\0";
            ofn.file = new string(new char[256]);
            ofn.maxFile = ofn.file.Length;
            ofn.fileTitle = new string(new char[64]);
            ofn.maxFileTitle = ofn.fileTitle.Length;
            string path = Application.streamingAssetsPath;
            path = path.Replace('/', '\\');
            //默认路径  
            // ofn.InitialDirectory = path;
            //ofn.InitialDirectory = "D:\\MyProject\\UnityOpenCV\\Assets\\StreamingAssets";  
            ofn.title = "Open Project";
            ofn.defExt = "JPG";//显示文件的类型  
            //注意 一下项目不一定要全选 但是0x00000008项不要缺少  
            ofn.flags = 0x00080000 | 0x00001000 | 0x00000800 | 0x00000200 | 0x00000008;//OFN_EXPLORER|OFN_FILEMUSTEXIST|OFN_PATHMUSTEXIST| OFN_ALLOWMULTISELECT|OFN_NOCHANGEDIR  
            if (WindowDll.GetOpenFileName(ofn))
            {
                UnityEngine.Debug.Log("Selected file with full path: " + ofn.file);
                StartCoroutine(IEHttpGet(ofn.file));
            }

        }
    }

    IEnumerator IEHttpGet(string imagePath)
    {
        WWW www = new WWW("localhost:8080/SteelPipe/appFront/celebaPredict.do?imagepath=" + imagePath);
        yield return www;

        if (www.error != null)
        {
            print(www.error);
        }
        print(www.text);
        ChangePic("neutral");
        Update();
    }

    private void ChangePic(string id)
    {
        texture = Resources.Load<Texture>("Textures/" + id);
        human.GetComponent<Renderer>().material.mainTexture = texture;
    }
}