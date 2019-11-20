using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Robot_visibility : MonoBehaviour {

    [SerializeField] private LayerMask wallLayerMask;

    // Mask
    [SerializeField] private Transform TopMask;
    [SerializeField] private Transform BottomMask;
    [SerializeField] private Transform LeftMask;
    [SerializeField] private Transform RightMask;

    // Use this for initialization
    void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
        // Adjust visibility
        Vector2 botLocation = transform.position;
        float boxCastTolerance = 0.1f;
        Vector2 boxCastSize = new Vector2(0.64f - boxCastTolerance, 0.64f - boxCastTolerance);
        RaycastHit2D top = Physics2D.BoxCast(botLocation, boxCastSize, 0.0f, Vector2.up, 0.64f, wallLayerMask);
        RaycastHit2D bottom = Physics2D.BoxCast(botLocation, boxCastSize, 0.0f, Vector2.down, 0.64f, wallLayerMask);
        RaycastHit2D left = Physics2D.BoxCast(botLocation, boxCastSize, 0.0f, Vector2.left, 0.64f, wallLayerMask);
        RaycastHit2D right = Physics2D.BoxCast(botLocation, boxCastSize, 0.0f, Vector2.right, 0.64f, wallLayerMask);
        Vector2 castCalibration;
        if (top)
        {
            castCalibration = top.point;
            castCalibration.x = botLocation.x;
            float castDist = (castCalibration - botLocation).magnitude - 0.32f;
            TopMask.localScale = new Vector2(1, castDist / 0.64f);
            TopMask.localPosition = Vector2.up * (castDist / 2.0f + 0.32f);
        }
        else
        {
            TopMask.localScale = new Vector2(1, 1);
            TopMask.localPosition = new Vector2(0, 0.64f);
        }
        if (bottom)
        {
            castCalibration = bottom.point;
            castCalibration.x = botLocation.x;
            float castDist = (castCalibration - botLocation).magnitude - 0.32f;
            BottomMask.localScale = new Vector2(1, castDist / 0.64f);
            BottomMask.localPosition = Vector2.down * (castDist / 2.0f + 0.32f);
        }
        else
        {
            BottomMask.localScale = new Vector2(1, 1);
            BottomMask.localPosition = new Vector2(0, -0.64f);
        }
        if (right)
        {
            castCalibration = right.point;
            castCalibration.y = botLocation.y;
            float castDist = (castCalibration - botLocation).magnitude - 0.32f;
            RightMask.localScale = new Vector2(castDist / 0.64f, 1);
            RightMask.localPosition = Vector2.right * (castDist / 2.0f + 0.32f);
        }
        else
        {
            RightMask.localScale = new Vector2(1, 1);
            RightMask.localPosition = new Vector2(0.64f, 0);
        }
        if (left)
        {
            castCalibration = left.point;
            castCalibration.y = botLocation.y;
            float castDist = (castCalibration - botLocation).magnitude - 0.32f;
            LeftMask.localScale = new Vector2(castDist / 0.64f, 1);
            LeftMask.localPosition = Vector2.left * (castDist / 2.0f + 0.32f);
        }
        else
        {
            LeftMask.localScale = new Vector2(1, 1);
            LeftMask.localPosition = new Vector2(-0.64f, 0);
        }
    }
}
