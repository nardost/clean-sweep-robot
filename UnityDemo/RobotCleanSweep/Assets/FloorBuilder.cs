using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.IO;

public class FloorBuilder : MonoBehaviour {
    string floorFilePathRelativeToAssets = "/../CleanSweepData/";//"/../../../sensor/src/main/resources/";
    [SerializeField] string floorFileName = "floor-plan-1.json";
    [SerializeField] private GameObject tilePrefab;
    [SerializeField] private GameObject wallPrefab;
    [SerializeField] private LayerMask wallLayerMask;
    [SerializeField] private LayerMask tileLayerMask;
    [SerializeField] private string passageLayer;
    [SerializeField] private string chargingStationLayer;

    [SerializeField] Sprite barefloorTexture;
    [SerializeField] Sprite lowpileTexture;
    [SerializeField] Sprite highpileTexture;

    [SerializeField] private Transform visibility;

    private List<GameObject> floorMap = new List<GameObject>();

    // Use this for initialization
    void Start () {
        //buildFloor(floorFileName);
    }
	
	// Update is called once per frame
	void Update () {
		
	}

    public void buildFloor(string fileName)
    {
        // Cleaning
        for(int i= 0;i< floorMap.Count; i++)
        {
            Destroy(floorMap[i]);
        }

        // Read File
        string floorFile = Application.streamingAssetsPath + floorFilePathRelativeToAssets + fileName;
        string JsonString = File.ReadAllText(floorFile);
        FloorJson floor = JsonUtility.FromJson<FloorJson>(JsonString);

        // Adjust Camera
        int width = floor.southEastCornerCoordinates.x;
        int height = floor.southEastCornerCoordinates.y;
        Camera.main.gameObject.transform.position = new Vector3(width * 0.64f / 2.0f, height * 0.64f / 2.0f, Camera.main.gameObject.transform.position.z);
        Camera.main.orthographicSize = Mathf.Max(width, height) / 2.0f;

        // Adjust visibility block
        Vector2 camPos = Camera.main.transform.position;
        visibility.position = camPos;
        visibility.localScale = new Vector2(Camera.main.orthographicSize / 0.64f, Camera.main.orthographicSize / 0.64f);

        // Create tiles
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                floorMap.Add(Instantiate(tilePrefab, intoWorld(i, j), Quaternion.identity));
            }
        }

        // Create Walls
        for (int i = 0; i < floor.obstacles.Count; i++)
        {
            Obstacle obs = floor.obstacles[i];

            // Vertical walls
            Color wallColor = Color.black;
            if (obs.from.x == obs.to.x)
            {
                Quaternion rot = Quaternion.Euler(0, 0, 90);
                float verticalCount = Mathf.Abs(obs.from.y - obs.to.y);
                for (int j = 0; j < verticalCount; j++)
                {
                    // Rotate wall
                    GameObject obstacle = Instantiate(wallPrefab);
                    floorMap.Add(obstacle);
                    obstacle.transform.rotation = rot;
                    obstacle.GetComponent<SpriteRenderer>().color = wallColor;
                    // 
                    float minY = Mathf.Min(obs.from.y, obs.to.y);
                    float posY = (minY + j) * 0.64f + 0.32f;
                    Vector2 pos = new Vector2(obs.from.x * 0.64f, posY);
                    obstacle.transform.position = pos;
                }
            }
            else
            {
                float horizontalCount = Mathf.Abs(obs.from.x - obs.to.x);
                for (int j = 0; j < horizontalCount; j++)
                {
                    // Rotate wall
                    GameObject obstacle = Instantiate(wallPrefab);
                    floorMap.Add(obstacle);
                    obstacle.GetComponent<SpriteRenderer>().color = wallColor;
                    // 
                    float minX = Mathf.Min(obs.from.x, obs.to.x);
                    float posX = (minX + j) * 0.64f + 0.32f;
                    Vector2 pos = new Vector2(posX, obs.from.y * 0.64f);
                    obstacle.transform.position = pos;
                }
            }
        }

        // Create Passages
        Color passageColor = Color.green;
        for (int i = 0; i < floor.passages.Count; i++)
        {
            passage pass = floor.passages[i];

            Vector2 from = intoWorld(pass.from.x, pass.from.y);
            Vector2 to = intoWorld(pass.to.x, pass.to.y);
            float dist = (from - to).magnitude;
            RaycastHit2D hit = Physics2D.Raycast(from, to - from, dist, wallLayerMask);
            //Debug.DrawRay(from, to - from, Color.red, 50);
            if (hit)
            {
                hit.collider.GetComponent<SpriteRenderer>().color = passageColor;
                hit.collider.gameObject.layer = LayerMask.NameToLayer(passageLayer);
            }
        }

        // Charging station
        for (int i = 0; i < floor.chargingStations.Count; i++)
        {
            Coordinate cs = floor.chargingStations[i];

            Vector2 csVec = intoWorld(cs.x, cs.y);
            Collider2D tileCollider = Physics2D.OverlapPoint(csVec, tileLayerMask);
            if (tileCollider != null)
            {
                tileCollider.GetComponent<SpriteRenderer>().color = Color.blue;
            }

        }

        // Areas
        for (int i = 0; i < floor.areas.Count; i++)
        {
            Area area = floor.areas[i];
            Sprite floorSprite = barefloorTexture;
            switch (area.floorType)
            {
                case "LOW_PILE":
                    floorSprite = lowpileTexture;
                    break;

                case "HIGH_PILE":
                    floorSprite = highpileTexture;
                    break;
            }

            Vector2 pointA = intoWorld(area.topLeft.x, area.topLeft.y);
            Vector2 pointB = intoWorld(area.bottomRight.x, area.bottomRight.y);
            Collider2D[] colliders = Physics2D.OverlapAreaAll(pointA, pointB, tileLayerMask);
            for(int j=0;j< colliders.Length; j++)
            {
                colliders[j].GetComponent<SpriteRenderer>().sprite = floorSprite;
            }
        }


    }

    Vector2 intoWorld(float x, float y)
    {
        return new Vector2(x * 0.64f + 0.32f, y * 0.64f + 0.32f);
    }

}

[System.Serializable]
public struct Coordinate
{
    public int x;
    public int y;
}
[System.Serializable]
public struct Obstacle
{
    public Coordinate from;
    public Coordinate to;
    public string obstacleType;
}
[System.Serializable]
public struct passage
{
    public Coordinate from;
    public Coordinate to;
}
[System.Serializable]
public struct Area
{
    public Coordinate topLeft;
    public Coordinate bottomRight;
    public string floorType;
}
[System.Serializable]
public class FloorJson
{
    public Coordinate southEastCornerCoordinates;
    public List<Obstacle> obstacles;
    public List<passage> passages;
    public List<Coordinate> chargingStations;
    public List<Area> areas;
}
