using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.IO;
using UnityEngine.UI;

public class Robot : MonoBehaviour {
    public bool Operate = false;
    string[] Operations = new string[] { };
    string MotionFilePath = "/../CleanSweepData/";
    [SerializeField] string MotionFile = "MotionFile.txt";
    float timeStep = 0.05f;
    float timeToPerform = 0;
    int operationIndex = 0;
    [SerializeField] private bool hasFinished = false;
    [SerializeField] private LayerMask tileLayerMask;

    // Interpolation
    private float InterpolatingRatio = 0.2f;
    private Vector2 realLocation = new Vector2(-1, -1);

    [SerializeField] Slider speedSlider;

    // Use this for initialization
    void Start () {
        
        //loadMotion(MotionFile);
    }
	public void loadMotion(string _fileName)
    {
        string fileName = Application.streamingAssetsPath + MotionFilePath + _fileName;
        Operations = File.ReadAllLines(fileName);
        operationIndex = 0;
        hasFinished = false;

        int x = (int)char.GetNumericValue(Operations[0][Operations[0].IndexOf('(') + 1]);
        int y = (int)char.GetNumericValue(Operations[0][Operations[0].IndexOf(')') - 1]);
        realLocation = intoWorld(x,y);
        SetLocation(x, y);
    }

	// Update is called once per frame
	void Update () {
        if (Operate)
        {
            // Adjusting speed
            float prevTimeStep = timeStep;
            timeStep = 1.0f / speedSlider.value;
            timeToPerform += timeStep - prevTimeStep;

            // Operate
            if (Time.time > timeToPerform)
            {
                timeToPerform = Time.time + timeStep;

                if(operationIndex < Operations.Length)
                {
                    Perform(Operations[operationIndex]);
                    operationIndex++;
                }
                else
                {
                    hasFinished = true;
                    Operate = false;
                }
            }

            // Interpolate
            if ((Vector2)transform.position != realLocation)
            {
                privInterpolate(timeStep, realLocation);
            }

            
        }
	}

    Vector2 intoWorld(float x, float y)
    {
        return new Vector2(x * 0.64f + 0.32f, y * 0.64f + 0.32f);
    }

    public void Perform(string operationString)
    {
        // Extract Location
        int x = -1, y = -1;
        if (operationString.Contains("("))
        {
            x = (int)char.GetNumericValue(operationString[operationString.IndexOf('(') + 1]);
            y = (int)char.GetNumericValue(operationString[operationString.IndexOf(')') - 1]);
        }
        SetLocation(x, y);

        // Extract event
        if (operationString.Contains("NOT CLEAN -->      CLEAN")) // Consume dirt
        {
            cleanDirtPile(x, y);
        }

    }
    private void cleanDirtPile(int x, int y)
    {
        Collider2D tileCollider = Physics2D.OverlapPoint(intoWorld(x, y), tileLayerMask);
        Color cleanColor = tileCollider.GetComponent<SpriteRenderer>().color;
        cleanColor.a = 0.5f;
        tileCollider.GetComponent<SpriteRenderer>().color = cleanColor;
    }

    public void SetLocation(int x, int y)
    {
        transform.position = realLocation;
        realLocation = intoWorld(x, y);
    }

    void privInterpolate(float _timeStep, Vector2 _InterpolateTo)
    {
        float interpolateDist = (1 / _timeStep) * 0.64f * Time.deltaTime;
        if ((_InterpolateTo - (Vector2)transform.position).sqrMagnitude >= interpolateDist * interpolateDist)
        {
            transform.position = (Vector2)transform.position + (_InterpolateTo - (Vector2)transform.position).normalized * interpolateDist;// Vector2.Lerp(transform.position, interpolateTo, InterpolatingRatio);
        }
        else
        {
            transform.position = _InterpolateTo;
        }
    }
}
