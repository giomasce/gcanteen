#!/bin/bash

inkscape -d 48 -e res/drawable-mdpi/ic_launcher.png artwork/scalable/ic_launcher.svg
inkscape -d 72 -e res/drawable-hdpi/ic_launcher.png artwork/scalable/ic_launcher.svg
inkscape -d 96 -e res/drawable-xhdpi/ic_launcher.png artwork/scalable/ic_launcher.svg
inkscape -d 144 -e res/drawable-xxhdpi/ic_launcher.png artwork/scalable/ic_launcher.svg
inkscape -d 512 -e ic_launcher-web.png artwork/scalable/ic_launcher.svg
