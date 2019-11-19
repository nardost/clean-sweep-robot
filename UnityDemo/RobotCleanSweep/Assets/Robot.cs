using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.IO;
using UnityEngine.UI;
using System;

public class Robot : MonoBehaviour {
    enum ParamIndex : int
    {
        Position = 0,
        Activity = 1,
        Battery = 2,
        Dirt = 3
    }

    [HideInInspector] public bool Operate = false;
    string[] Operations = new string[] { };
    string MotionFilePath = "/../CleanSweepData/";
    float timeStep = 0.05f;
    float timeToPerform = 0;
    int operationIndex = 0;
    private bool hasFinished = false;
    [SerializeField] private LayerMask tileLayerMask;

    // Interpolation
    private Vector2 realLocation = new Vector2(-1, -1);

    [SerializeField] Slider speedSlider;
    public bool negated = false;

    // Meters
    [SerializeField] Text batteryLevel;
    [SerializeField] Text dirtLevel;
    [SerializeField] Bar_generic batteryBar;
    [SerializeField] Bar_generic dirtBar;
    const float maxBattery = 250;
    const int maxDirt = 50;

    [SerializeField] Charging_Effect robotBlinkModule;

    // Hard-coded time
    const int scheduleScaleTime = 3;
    const int chargingScaleTime = 3;

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

        // Hard Set initial position
        string[] firstOperationSplit = Operations[0].Split();
        Vector2 startPosition = extractPosition(firstOperationSplit[(int)ParamIndex.Position]);
        SetLocationInstant((int)startPosition.x, (int)startPosition.y);

        // Perform the first activity
        Perform(Operations[0]);
    }

    // Update is called once per frame
    void Update () {
        //if (Operate)
        //{
            // Adjusting speed
            float prevTimeStep = timeStep;
            timeStep = 1.0f / speedSlider.value;

            
            timeToPerform += timeStep - prevTimeStep;
            
            // Operate
            if (Time.time > timeToPerform)
            {
                timeToPerform = Time.time + timeStep;



                if(operationIndex < Operations.Length && operationIndex >= 0)
                {
                    Perform(Operations[operationIndex]);
                    if (!negated)
                    {
                        operationIndex = Mathf.Clamp(operationIndex + 1, 0, Operations.Length - 1);
                    }
                    else
                    {
                        operationIndex = Mathf.Clamp(operationIndex - 1, 0, Operations.Length - 1);
                    }
                   
                }
                //else
                //{
                //    hasFinished = true;
                //    Operate = false;
                //}
            }

            // Interpolate
            if ((Vector2)transform.position != realLocation)
            {
                privInterpolate(timeStep, realLocation);
            }
        //}
	}

    Vector2 intoWorld(float x, float y)
    {
        return new Vector2(x * 0.64f + 0.32f, y * 0.64f + 0.32f);
    }

    Vector2 extractPosition(string parameterString)
    {
        
        int xLen = parameterString.IndexOf(',') - parameterString.IndexOf('[') - 1;
        int x = int.Parse(parameterString.Substring(parameterString.IndexOf('[')+1, xLen));

        int yLen = parameterString.IndexOf(']') - parameterString.IndexOf(',') - 1;
        int y = int.Parse(parameterString.Substring(parameterString.IndexOf(',') + 1, yLen));
        //Debug.Log("string: " + parameterString + "; x" + x + ", y" + y);
        return new Vector2(x, y);
    }

    float extractBatteryLevel(string parameterString)
    {
        int coordinateLen = parameterString.IndexOf(']') - parameterString.IndexOf('[') + 1 - 2;
        string coordinate = parameterString.Substring(parameterString.IndexOf('[') + 1, coordinateLen);
        return float.Parse(coordinate);
    }

    int extractDirtLevel(string parameterString)
    {
        int dirtLen = parameterString.IndexOf(']') - parameterString.IndexOf('[') + 1 - 2;
        string dirt = parameterString.Substring(parameterString.IndexOf('[') + 1, dirtLen);
        return int.Parse(dirt);
    }

    string extractEvent(string parameterString)
    {
        int eventLen = parameterString.IndexOf(']') - parameterString.IndexOf('[') + 1 - 2;
        string eventString = parameterString.Substring(parameterString.IndexOf('[') + 1, eventLen);
        return eventString;
    }

    public void Perform(string operationString)
    {
        //Split operation
        string[] operationStringSplits = operationString.Split();

        // Extract Location
        int x = -1, y = -1;
        Vector2 pos = extractPosition(operationStringSplits[(int)ParamIndex.Position]);
        x = (int)pos.x;
        y = (int)pos.y;
        

        // Extract Battery Level
        float battery = -1;
        battery = extractBatteryLevel(operationStringSplits[(int)ParamIndex.Battery]);

        // Extract Dirt Level
        int dirt = -1;
        dirt = extractDirtLevel(operationStringSplits[(int)ParamIndex.Dirt]);


        // Extract Battery Level
        string eventString;
        eventString = extractEvent(operationStringSplits[(int)ParamIndex.Activity]);
        privBlinkState();
        switch (eventString)
        {
            case "CLEAN":
                cleanDirtPile(x, y);
                break;
            //case "ALREADY_CLEAN":
            //    break;
            case "DIRT_FULL":
                robotBlinkModule.SetBlinkColor(Color.blue);
                robotBlinkModule.enabled = true;
                break;
            //case "DIRT_CLEAN":
            //    break;
            //case "GO_CHARGE":
            //    break;
            case "CHARGING":
                robotBlinkModule.SetBlinkColor(Color.red);
                robotBlinkModule.enabled = true;
                break;
            //case "RESUME":
            //    break;
            //case "STANDBY":
            //    break;
        }

        // Update battery and dirt level
        SetLocation(x, y);
        privUpdateBattery(battery);
        privUpdateDirt(dirt);
    }

    private void privBlinkState()
    {
        robotBlinkModule.enabled = false;
    }

    private void privUpdateBattery(float level)
    {
        batteryLevel.text = level.ToString();
        batteryBar.SetProgress(level / maxBattery);
    }
    private void privUpdateDirt(float level)
    {
        dirtLevel.text = level.ToString();
        dirtBar.SetProgress((float)level / (float)maxDirt);
    }

    private void cleanDirtPile(int x, int y)
    {
        Collider2D tileCollider = Physics2D.OverlapPoint(intoWorld(x, y), tileLayerMask);
        Color cleanColor = tileCollider.GetComponent<SpriteRenderer>().color;
        if (!negated) {
            cleanColor.a = 0.5f;
        }
        else
        {
            cleanColor.a = 1.0f;
        }
        tileCollider.GetComponent<SpriteRenderer>().color = cleanColor;
    }

    public void SetLocation(int x, int y)
    {
        transform.position = realLocation;
        realLocation = intoWorld(x, y);
    }
    public void SetLocationInstant(int x, int y)
    {
        realLocation = intoWorld(x, y);
        transform.position = realLocation;
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
