using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class ControlChatRoom : MonoBehaviour
{
    public InputField chatInput;
    public Text chatText;
    public ScrollRect scrollRect;
    private string username = "Me";
    private string emotion = "(normal)";

    void Start()
    {
        chatInput.ActivateInputField();
    }

    void Update()
    {
        chatInput.ActivateInputField();
        if (Input.GetKeyDown(KeyCode.Return) || Input.GetKeyDown(KeyCode.KeypadEnter))
        {
            if (chatInput.text != "")
            {
                StartCoroutine(IEHttpGet(chatInput.text));
            }
        }
    }

    IEnumerator IEHttpGet(string text)
    {
        WWW www = new WWW("localhost:8080/ZHCNBACKEND/ToneServlet?text=" + text);
        yield return www;

        if (www.error != null)
        {
            print(www.error);
        }
        print(www.text);
        if (www.text.Equals(""))
        {
        }
        else if (www.text.Equals("anger"))
        {
            emotion = "(anger)";
        }
        else if (www.text.Equals("sadness"))
        {
            emotion = "(sadness)";
        }
        else if (www.text.Equals("confident"))
        {
            emotion = "(confident)";
        }
        string addText = "\n" + emotion + "<color=red>" + username + "</color>:   " + chatInput.text;
        chatText.text += addText;
        chatInput.text = "";
        chatInput.ActivateInputField();
        Canvas.ForceUpdateCanvases();
        scrollRect.verticalNormalizedPosition = 0f;
        Canvas.ForceUpdateCanvases();
        emotion = "(normal)";
    }
}