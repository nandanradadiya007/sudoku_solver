from setuptools import setup

APP = ['sudoku_gui.py']
OPTIONS = {
    'argv_emulation': True,
    'includes': ['PyQt5'],
    'packages': ['PyQt5'],
}

setup(
    app=APP,
    options={'py2app': OPTIONS},
    setup_requires=['py2app'],
)
