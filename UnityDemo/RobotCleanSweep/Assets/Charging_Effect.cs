using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Charging_Effect : MonoBehaviour {
   
    [SerializeField] SpriteRenderer robotSprite;
    float blinkInterval = 0.2f;
    float timeToBlink = 0;
    private Color colorOriginal;
    private Color color2 = Color.red;
    // Use this for initialization
    void Start () {
        colorOriginal = robotSprite.color;

    }
	
	// Update is called once per frame
	void Update () {
		if(Time.time > timeToBlink)
        {
            timeToBlink = Time.time + blinkInterval;
            if(robotSprite.color == color2)
            {
                robotSprite.color = colorOriginal;
            }
            else
            {
                robotSprite.color = color2;
            }
        }
	}

    public void SetBlinkColor(Color blinkColor)
    {
        color2 = blinkColor;
    }

    private void OnDisable()
    {
        robotSprite.color = colorOriginal;
    }
}
