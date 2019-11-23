using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class Bar_generic : MonoBehaviour {
    private float initWidth;
    [SerializeField] RectTransform bar;
    [SerializeField] Color zeroColor;
    [SerializeField] Color fullColor;

    // Use this for initialization
    void Start () {
        initWidth = GetComponent<RectTransform>().rect.width;
    }
	
	// Update is called once per frame
	public void SetProgress(float ratio)
    {
        bar.offsetMax = new Vector2(-initWidth * (1.0f - ratio), bar.offsetMax.y);
        bar.GetComponent<Image>().color = Color.Lerp(zeroColor, fullColor, ratio);
    }
}
