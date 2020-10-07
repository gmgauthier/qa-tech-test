import pytest
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.wait import WebDriverWait
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.support import expected_conditions as EC


driver = None


def setup_module(module):
    global driver
    options = webdriver.ChromeOptions()
    options.add_argument('--ignore-certificate-errors')
    driver = webdriver.Chrome(ChromeDriverManager().install(), options=options)


def teardown_module(module):
    driver.quit()


def test_enter_partition_values():
    driver.get('http://localhost:3000/')
    driver.find_element(By.XPATH, "//*[@data-test-id='render-challenge']").click()
    # will poll until the table is visible:
    WebDriverWait(driver, 1).until(EC.element_to_be_clickable((By.TAG_NAME, 'tr')))

    rows = driver.find_elements(By.TAG_NAME, "tr")
    for row in enumerate(rows):
        row_data = row[1].find_elements(By.TAG_NAME, "td")
        row_array = [int(item.text) for item in row_data]
        # print(row_array, find_partition(row_array))
        driver.find_element(By.XPATH, "//*[@data-test-id='submit-"+str(row[0]+1)+"']").send_keys(find_partition(row_array))

    driver.find_element(By.XPATH, "//*[@data-test-id='submit-4']").send_keys("Greg Gauthier")
    driver.find_element(By.XPATH, "//*[@id='challenge']/div/div/div[2]/div/div[2]/button").click()
    results_box = "body > div:nth-child(5) > div > div:nth-child(1) > div > div > div:nth-child(1)"
    WebDriverWait(driver, 1).until(EC.element_to_be_clickable((By.CSS_SELECTOR, results_box)))
    results = driver.find_element(By.CSS_SELECTOR, "body > div:nth-child(5) > div > div:nth-child(1) > div > div > div:nth-child(1)")
    fail_text = "It looks like your answer wasn't quite right"
    assert fail_text not in results.text


def find_partition(arr):
    arrlen = len(arr)
    leftside_sum = [0] * arrlen
    leftside_sum[0] = arr[0]
    for i in range(1, arrlen):
        leftside_sum[i] = leftside_sum[i - 1] + arr[i]

    rightside_sum = [0] * arrlen
    rightside_sum[arrlen - 1] = arr[arrlen - 1]
    for i in range(arrlen - 2, -1, -1):
        rightside_sum[i] = rightside_sum[i + 1] + arr[i]

    for i in range(1, arrlen - 1, 1):
        if leftside_sum[i] == rightside_sum[i]:
            return arr[i]
    return None
##
# Solution
#
# [23, 50, 63, 90, 10, 30, 155, 23, 18] 10
# [133, 60, 23, 92, 6, 7, 168, 16, 19] 92
# [30, 43, 29, 10, 50, 40, 99, 51, 12] 40
##
