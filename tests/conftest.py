"""Configure pytest."""
import pytest
from mockito import unstub

@pytest.fixture(autouse=True)
def unstub_fixture():
    """Unstub python mockito."""
    yield
    unstub()
