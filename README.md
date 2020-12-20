# DarwinSimulation  <image src="screenshots/icon.png" width=100>
[Project for Object Oriented Programming](https://github.com/apohllo/obiektowe-lab/tree/master/lab8)

---

<a type="anchor" id="toc-bullet"></a>
## Table of contents

[About the project](#about-bullet)

[Parameters](#params-bullet)

[Additional features](#ui-bullet)

[Notes](#notes-bullet)

---

<a type="anchor" id="about-bullet"></a>
# About

<!--suppress ALL -->
<center>
    <image src="screenshots/app.png" width=800>
    <br/>
    <i> Single simulation window </i>
</center>

## Key functionalities

- Real-time statistics
- Two simultaneous simulations
- Displaying animals with dominant genotype *(changes their bar colour from blue to red)*
- Choosing animal to watch their additional statistics *(see additional features)*
  - Number of children
  - Number of descendants
  - Day of death
- Saving mean statistics from period to file *(see additional features)*
- Population and vegetation graph *(single simulation only)*
- Genes distribution *(single simulation only)*


---

<a type="anchor" id="params-bullet"></a>
#Parameters

Parameters can be passed to a simulation using Form at the beginning of the program or JSON parameters files. All fields must be filled to start the simulation.

<center>
    <image src="screenshots/param.png" width=200/>
    <br/>
    <i>Setup window</i>
</center>

```json
parameters.json
{
  "width" : 10,
  "height" : 10,
  "startEnergy" : 10,
  "moveEnergy" : 1,
  "plantEnergy" : 10,
  "startingAnimals" : 10,
  "jungleRatio" : 0.2
}
```
*Example JSON file (all fields must be included):*

---

<a type="anchor" id="ui-bullet"></a>

## Additional information about the animal

User can choose an animal and observe the number of new family members for a given time. To do this, firstly you have to type in duration to the text field *Number of ages*. Next, pause the simulation and click on the animal (if the tile contains more than one animal, the strongest will be chosen). After that simulation will update information about the watched animal (activation is indicated by text *watching*). For the next *N* days program will count the number of new children and descendants of the animal, and if the animal dies, it will display its day of death.

## Saving mean statistic for a period of time

User can also gather mean statistics from a given period. To do so, type in duration to the text field *Duration*, and the filename. After that, you can hit *Generate* button. The simulation will wait till the end of the period, saving average data to the specified file.


---
<a type="anchor" id="notes-bullet"></a>
## Notes

Simulation won't start if the chosen file is corrupted *(i,e. isn't JSON or doesn't contain all fields)*

The simulation also won't start if all fields aren't filled or amount of starting animals is larger than the size of the map.

User can change followed animal clicking again on the map during the pause.
