using UnityEngine;
using UnityEngine.UI;

using System.Collections;


using System.Collections.Generic;

public class AnimationV : MonoBehaviour {

	

private string folder = "Yamato";
bool MakeTexture = true;
private Object[] jpegs;
private List<Object> frames = new List<Object>();
int counter = 0;
int counter2 = 0;
bool Film = true;
float PictureRateInSeconds = 1;
private float nextPic = 0;
private int pics = 0;
private Renderer screen;

public bool loop = false;
public GameObject scene;
public Text debug;
public int sceneN = 1; 
public static bool start = false;

void preInit() {
	if(sceneN == 1) { folder = "folder"; }
}

void Init() {
	if(sceneN == 1) { pics = 86; }
}

void Start () {

	preInit();
	Init();

	screen = GetComponent<Renderer>();
	screen.enabled = false;

	if (Film == true)
         {
             PictureRateInSeconds = 0.067f;
         }
	jpegs = Resources.LoadAll(folder, typeof(Texture));

         for (int i = 0; i < pics; i++)
         {
             frames.Add(jpegs[i]);
         }
}
	
void Update () {
	
	Init();	

	if (Time.time > nextPic && start == true && counter < pics-1)
         {	
             screen.enabled = true;
             nextPic = Time.time + PictureRateInSeconds;
             counter++;
		
             if (MakeTexture)
             {
                screen.material.mainTexture = (Texture) frames[counter];
             }
         }
	if (counter >= pics-1)
         {

             ++counter2;
		if(counter2 == 30) { scene.SetActive(false); }
         }
}

}
