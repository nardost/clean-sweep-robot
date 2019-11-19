using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class FileReaderManager : MonoBehaviour {
    [SerializeField] Robot robot;
    [SerializeField] FloorBuilder floorBuilder;

    [SerializeField] InputField mocapFile;
    [SerializeField] InputField floorFile;

    [SerializeField] SpriteRenderer visibility;

    // Use this for initialization
    void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}
    public void Build()
    {
        floorBuilder.buildFloor(floorFile.text);

        robot.loadMotion(mocapFile.text);

        robot.Operate = false;
    }
    public void Run()
    {
        floorBuilder.buildFloor(floorFile.text);

        robot.loadMotion(mocapFile.text);

        robot.Operate = true;
    }
    public void ToggleVisibility()
    {
        if (visibility.enabled)
        {
            visibility.enabled = false;
        }
        else
        {
            visibility.enabled = true;
        }
    }
    public void ToggleNegate()
    {
        if (robot.negated)
        {
            robot.negated = false;
        }
        else
        {
            robot.negated = true;
        }
    }
}
