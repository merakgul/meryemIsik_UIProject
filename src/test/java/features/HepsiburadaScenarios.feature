Feature: Hepsiburada UI Test Automation

  Scenario: User adds a product to the cart and verifies the price
    Given The user navigates to the Hepsiburada website
    When The user navigates to the Electronics -> Tablet category
    When The user applies filters for Brand -> "Apple" and Screen Size -> "13,2 inç"
    And The user clicks on the highest priced product
    And The user adds the product to the cart
    Then The user verifies that the product is added to the cart and the price is correct
