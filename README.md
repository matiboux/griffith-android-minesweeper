# MineSweeper

Second project assignment for the **Mobile Development** module at **Griffith College**.

Open-source repository: https://github.com/matiboux/griffith-android-minesweeper (private).

Play Store page: _Not published yet._

Alternatively, see the [latest release](https://github.com/matiboux/griffith-android-minesweeper/releases/latest) for downloading the APK.


## Documentation

_Not available yet._


## Milestones

- [ ] Define the shell of a custom view class and generate a layout for the main activity consisting of the
custom view, two buttons, and two textviews.  
(10%)
- [ ] Draw the initial state of the game board with all cells covered. Black should be used as the fill colour
and white lines should separate the cells.  
(20%)
- [ ] Implement the basic touch behaviour that will uncover a cell. When a cell is uncovered it should
change from a black colour to a grey colour.  
(40%)
- [ ] Implement methods to place 20 mines randomly in the minefield and render the mines when a cell is
uncovered. A cell containing a mine should have a red background with a black M in the foreground.  
(60%)
- [ ] Modify the touch behaviour to stop accepting input when a mine is uncovered. It should only reenable
input when the user has clicked the reset button.  
(80%)
- [ ] The other button should switch between displaying “uncover mode” or “marking mode” each time it
is clicked. In uncover mode each touch should result in a cell being uncovered. In marking mode each
time a covered cell is touched, it should change to yellow to denote it is marked or back to black, to
denote it is unmarked. Use the two text fields to denote the total number of mines and the number of
mines marked. In uncover mode, touching a marked cell should do nothing.  
(100%)


## Authors

- **Mathieu GUÉRIN** – [matiboux.me](https://matiboux.me/)
